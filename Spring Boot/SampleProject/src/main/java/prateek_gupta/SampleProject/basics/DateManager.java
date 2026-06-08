package prateek_gupta.SampleProject.basics;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Question:
 * Create a method which add or subtract number of days from today's date without
 * using built-in classes
 * <br>
 * Imp Trick
 * For calculating leap years in a range of years, first check number of leap years in range
 * excluding first and last year, and then check if first or last year is a leap year and
 * in counting the extra day 29th of May will be considered.
 */
public class DateManager {
    public int year;
    /**
     * Jan will be 1
     */
    public int month;
    public int dayOfMonth;


    public DateManager(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    /**
     * The approach is first we calculate for years, then months and then days.
     */
    public DateManager updateDayOfMonth(Integer noOfDays) {
        // Processing Year(Using abs() so that we get non-negative number for checking if we
        // have to change year or not)
        if (Math.abs(noOfDays) >= 365) {
            // Calculating number of years(+/- will be automatically handled)
            int noOfYear = noOfDays / 365;

            int leapYears = getLeapYearsCount(noOfYear);

            // Updating year
            this.year += noOfYear;

            // Updating no of days after removing years and leap years
            noOfDays = (noOfDays % 365) - leapYears;

        }

        while (noOfDays != 0) {
            if (noOfDays >= 0) {
                int noOfDaysInMonth;
                if (Arrays.asList(1, 3, 5, 7, 8, 10, 12).contains(this.month))
                    noOfDaysInMonth = 31;
                else if (Arrays.asList(4, 6, 9, 11).contains(this.month))
                    noOfDaysInMonth = 30;
                else noOfDaysInMonth = this.year % 4 == 0 ? 29 : 28;

                // Trying to come to starting of the month if possible
                if (noOfDays > (noOfDaysInMonth - this.dayOfMonth)) {
                    noOfDays -= (noOfDaysInMonth - this.dayOfMonth) + 1;
                    this.month += 1;
                    this.dayOfMonth = 1;
                }
                // Updating number of days
                else {
                    this.dayOfMonth += noOfDays;
                    noOfDays = 0;
                }

            } else {
                int noOfDaysInLastMonth;
                if (Arrays.asList(1, 3, 5, 7, 8, 10, 12).contains(this.month - 1))
                    noOfDaysInLastMonth = 31;
                else if (Arrays.asList(4, 6, 9, 11).contains(this.month - 1))
                    noOfDaysInLastMonth = 30;
                else noOfDaysInLastMonth = this.year % 4 == 0 ? 29 : 28;

                // Trying to come to ending of the last month if possible(
                // using - in (-this.dayOfMonth) because noOfDays is -ve)
                if (noOfDays < (-this.dayOfMonth)) {
                    noOfDays += this.dayOfMonth;
                    this.dayOfMonth = noOfDaysInLastMonth;
                    this.month -= 1;
                }
                // Updating number of days
                else {
                    this.dayOfMonth += noOfDays;
                    noOfDays = 0;
                }
            }
        }


        return this;
    }

    /**
     * This method get number of leap years between the current value of field
     * {@link DateManager#year} and the targeted year we get after adding/subtracting
     * number of years to be added or removed specified by argument noOfYear
     */
    private int getLeapYearsCount(int noOfYear) {
        int leapYears = 0;

        // When number of years are supposed to be added
        if (noOfYear > 0) {
            // Calculating leap years in the range from current year to targeted year
            // (excluding the current and target years), if no of year is more than 1
            if (noOfYear > 1) {
                int nextLeapYearInRange = (this.year + 1) % 4 == 0 ?
                        (this.year + 1) :
                        (this.year + 1) + (4 - (this.year + 1) % 4);

                int lastLeapYearInRange = (this.year + noOfYear - 1) % 4 == 0 ?
                        (this.year + noOfYear - 1) :
                        (this.year + noOfYear - 1) - ((this.year + noOfYear - 1) % 4);

                // Getting total number of leap years.
                // Also, the +1 at last is because all the leap years have to be included but
                // while subtracting we usually don't count the starting value so to balance that
                // we add 1
                leapYears = ((lastLeapYearInRange - nextLeapYearInRange) / 4) + 1;
            }

            // Checking if current year is a leap year and if the extra day 29th of Feb will be
            // part of counting or not, if yes then we increase number of leap years count
            if (this.year % 4 == 0 && this.month <= 2 && this.dayOfMonth <= 28)
                leapYears += 1;

            // Checking if targeted year is a leap year and if the extra day 29th of Feb will be
            // part of counting or not, if yes then we increase number of leap years count
            if (((this.year + noOfYear) % 4 == 0) && this.month >= 3 && this.dayOfMonth >= 1)
                leapYears += 1;
        }
        // When number of years are supposed to be subtracted
        else {
            // Calculating leap years in the range from targeted year to current year
            // (excluding the current and target years), if no of year is less than 1
            if (noOfYear < -1) {
                int firstLeapYearInRange = (this.year + noOfYear + 1) % 4 == 0 ?
                        (this.year + noOfYear + 1) :
                        (this.year + noOfYear + 1) + (4 - (this.year + noOfYear + 1) % 4);
                int lastLeapYearInRange = (this.year - 1) % 4 == 0 ?
                        (this.year - 1) :
                        (this.year - 1) - ((this.year - 1) % 4);

                // Getting total number of leap years.
                // Also, the +1 at last is because all the leap years have to be included but
                // while subtracting we usually don't count the starting value so to balance that
                // we add 1
                leapYears = ((lastLeapYearInRange - firstLeapYearInRange) / 4) + 1;
            }

            // Checking if current year is a leap year and if the extra day 29th of Feb will be
            // part of counting or not, if yes then we increase number of leap years count
            if (this.year % 4 == 0 && this.month >= 3 && this.dayOfMonth >= 1)
                leapYears += 1;

            // Checking if targeted year is a leap year and if the extra day 29th of Feb will be
            // part of counting or not, if yes then we increase number of leap years count
            if (((this.year + noOfYear) % 4 == 0) && this.month <= 2 && this.dayOfMonth <= 28)
                leapYears += 1;

            // Changing the sign as number of days needs to be removed for leap years
            leapYears = -leapYears;
        }

        return leapYears;
    }

    @Override
    public String toString() {
        return year + "/" + month + "/" + dayOfMonth;
    }

    public static void main(String[] args) {
        int y = 2026;
        int m = 5;
        int d = 4;
        int v = -3700;
        long startTime = System.currentTimeMillis();
        Calendar date1 = Calendar.getInstance();
        date1.set(Calendar.YEAR, y);
        date1.set(Calendar.MONTH, (m - 1));
        date1.set(Calendar.DAY_OF_MONTH, d);

        date1.add(Calendar.DAY_OF_MONTH, v);

        System.out.println(date1.get(Calendar.YEAR) + "/" +
                (date1.get(Calendar.MONTH) + 1) + "/" +
                date1.get(Calendar.DAY_OF_MONTH));
        System.out.println("Built-in Approach time : " +
                (System.currentTimeMillis() - startTime) + "ms");

        DateManager date = new DateManager(y, m, d);
        startTime = System.currentTimeMillis();
        System.out.println(date.updateDayOfMonth(v));
        System.out.println("My Approach time : " +
                (System.currentTimeMillis() - startTime) + "ms");
    }
}
