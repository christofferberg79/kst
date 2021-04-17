import cberg.kst.Activity
import cberg.kst.InvoiceReport
import cberg.kst.TimeReport
import cberg.kst.TimeRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import kotlin.test.Test
import kotlin.test.assertEquals

class KstTest {

    /*
    kst value change: invoice amount * my share
    my share: by date
              OR by accumulated invoice amount
    -invoice amount: hours * rate
    -rate: by activity
    -activity: by time report
    -hours: by time report
    -date: by time report
     */

    @Test
    fun testInvoiceReport() {
        val timeRepository = InMemoryTimeRepository()
        timeRepository.add(Activity(1, BigDecimal(1_000)))
        timeRepository.add(Activity(2, BigDecimal(2_000)))
        timeRepository.add(TimeReport(LocalDate.of(2021, 1, 15), 8, 1))
        timeRepository.add(TimeReport(LocalDate.of(2021, 2, 15), 8, 1))
        timeRepository.add(TimeReport(LocalDate.of(2021, 2, 25), 6, 2))
        val invoiceReport = InvoiceReport(timeRepository)
        val amounts = invoiceReport.getAmountsByMonth()

        assertEquals(2, amounts.size)
        assertEquals(BigDecimal(8_000), amounts[YearMonth.of(2021, 1)])
        assertEquals(BigDecimal(20_000), amounts[YearMonth.of(2021, 2)])
    }

}

class InMemoryTimeRepository : TimeRepository {
    private val activities = mutableMapOf<Long, Activity>()
    private val timeReports = mutableListOf<TimeReport>()

    override fun add(activity: Activity) {
        activities[activity.id] = activity
    }

    override fun add(timeReport: TimeReport) {
        timeReports += timeReport
    }

    override fun getActivity(id: Long): Activity {
        return activities[id] ?: error("Invalid activty ID $id")
    }

    override fun getTimeReports(): List<TimeReport> = timeReports
}