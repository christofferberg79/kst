package cberg.kst

import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class Activity(val id: Long, val rate: BigDecimal)
class TimeReport(val date: LocalDate, val hours: Int, val activityId: Long)


interface TimeRepository {
    fun add(activity: Activity)
    fun add(timeReport: TimeReport)

    fun getActivity(id: Long): Activity
    fun getTimeReports(): List<TimeReport>
}

class InvoiceReport(private val timeRepository: TimeRepository) {
    fun getAmountsByMonth(): SortedMap<YearMonth, BigDecimal> {
        return timeRepository.getTimeReports()
            .groupBy(this::getYearMonth, this::getInvoiceAmount)
            .mapValues { (_, invoiceAmounts) -> invoiceAmounts.sum() }
            .toSortedMap()
    }

    private fun getYearMonth(timeReport: TimeReport) = YearMonth.from(timeReport.date)
    private fun getInvoiceAmount(timeReport: TimeReport) = calculateInvoiceAmount(timeReport.hours, getRate(timeReport))
    private fun getRate(timeReport: TimeReport) = getActivity(timeReport.activityId).rate
    private fun getActivity(id: Long) = timeRepository.getActivity(id)
    private fun calculateInvoiceAmount(hours: Int, rate: BigDecimal) = rate * hours.toBigDecimal()
}

