package src;

import java.util.Arrays;
import java.util.Calendar;

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

    public DateManager updateDayOfMonth(Integer noOfDays) {
        if (Math.abs(noOfDays) >= 365) {
            // Calculating number of years
            int noOfYear = noOfDays / 365;


            int leapYears = getLeapYears(noOfYear);

            // Updating no of days and year
            noOfDays = (noOfDays % 365) - leapYears;
            this.year += noOfYear;
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
                } else {
                    this.dayOfMonth += noOfDays;
                    noOfDays = 0;
                }

            } else {
                int noOfDaysInMonth;
                if (Arrays.asList(1, 3, 5, 7, 8, 10, 12).contains(this.month - 1))
                    noOfDaysInMonth = 31;
                else if (Arrays.asList(4, 6, 9, 11).contains(this.month - 1))
                    noOfDaysInMonth = 30;
                else noOfDaysInMonth = this.year % 4 == 0 ? 29 : 28;

                // Trying to come to ending of the last month if possible
                if (noOfDays < (-this.dayOfMonth)) {
                    noOfDays += this.dayOfMonth;
                    this.dayOfMonth = noOfDaysInMonth;
                    this.month -= 1;
                } else {
                    this.dayOfMonth += noOfDays;
                    noOfDays = 0;
                }
            }
        }


        return this;
    }

    private int getLeapYears(int noOfYear) {
        int leapYears = 0;

        if (noOfYear > 0) {
            // Calculating leap years in the range from current years to targeted year, if
            // no of year is more than 1
            if (noOfYear > 1) {
                int starting = (this.year + 1) % 4 == 0 ?
                        (this.year + 1) :
                        (this.year + 1) + (4 - (this.year + 1) % 4);
                int ending = (this.year + noOfYear - 1) % 4 == 0 ?
                        (this.year + noOfYear - 1) :
                        (this.year + noOfYear - 1) - ((this.year + noOfYear - 1) % 4);
                leapYears = ((ending - starting) / 4) + 1;
            }

            // Checking if current year is a leap year and if we have to consider it
            if (this.year % 4 == 0 && this.month <= 2 && this.dayOfMonth <= 28)
                leapYears += 1;

            // Checking if targeted year is a leap year and if we have to consider it
            if (((this.year + noOfYear) % 4 == 0) && this.month >= 3 && this.dayOfMonth >= 1)
                leapYears += 1;
        } else {
            if (noOfYear < -1) {
                int starting = (this.year + noOfYear + 1) % 4 == 0 ?
                        (this.year + noOfYear + 1) :
                        (this.year + noOfYear + 1) + (4 - (this.year + noOfYear + 1) % 4);
                int ending = (this.year - 1) % 4 == 0 ?
                        (this.year - 1) :
                        (this.year - 1) - ((this.year - 1) % 4);

                leapYears = (((ending / 4) - (starting / 4)) + 1);
            }

            // Checking if current year is a leap year and if we have to consider it
            if (this.year % 4 == 0 && this.month >= 3 && this.dayOfMonth >= 1)
                leapYears += 1;

            // Checking if targeted year is a leap year and if we have to consider it
            if (((this.year + noOfYear) % 4 == 0) && this.month <= 2 && this.dayOfMonth <= 28)
                leapYears += 1;

            // Changing the sign as number of days will be added for leap years
            leapYears = -leapYears;
        }

        return leapYears;
    }

    @Override
    public String toString() {
        return year + "/" + month + "/" + dayOfMonth;
    }

    public static void main(String[] args) {
        int y = 2024;
        int m = 2;
        int d = 5;
        int v = -1095;
        Calendar date1 = Calendar.getInstance();
        date1.set(Calendar.YEAR, y);
        date1.set(Calendar.MONTH, (m - 1));
        date1.set(Calendar.DAY_OF_MONTH, d);

        date1.add(Calendar.DAY_OF_MONTH, v);

        System.out.println(date1.get(Calendar.YEAR) + "/" +
                (date1.get(Calendar.MONTH) + 1) + "/" +
                date1.get(Calendar.DAY_OF_MONTH));

        DateManager date = new DateManager(y, m, d);
        System.out.println(date.updateDayOfMonth(v));
    }
}
