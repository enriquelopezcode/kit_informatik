package edu.kit.kastel;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that represents an advent calendar which contains a list of candies for each day.
 * The class allows tracking of the current day, opening doors to retrieve candies, and resetting the calendar.
 *
 * @author ukgyh
 */

public class AdventCalendar {
    private static final int LINE_BREAK_FREQUENCY = 4;
    private final int finalDay;
    private List<Candy> candies;

    private final List<Candy> candiesBackup;
    private int currentDay = 0;
    private final List<Boolean> doors;



    /**
     * Constructs an AdventCalendar with a list of candies.
     * Initializes the calendar with all doors closed.
     * Sets final day of the calendar (not limited to 24)
     *
     * @param candies       The list of Candy objects for each day of the advent calendar.
     */

    public AdventCalendar(List<Candy> candies) {
        this.finalDay = candies.size();
        this.candies = candies;

        //candy backup for resets
        this.candiesBackup = deepCopyCandies(candies);

        this.doors = new ArrayList<>();

        //all doors are closed (false) initially
        for (int i = 0; i < finalDay; i++) {
            doors.add(false);
        }
    }

    /**
     * Makes a deep copy of all candy objects from one List and returns the copy.
     *
     * @param originList List from which to copy candy objects
     * @return list containing the copied candy objects
     */
    private List<Candy> deepCopyCandies(List<Candy> originList) {
        List<Candy> copiedList = new ArrayList<>();
        for (Candy candy : originList) {
            copiedList.add(candy.copy());
        }

        return copiedList;
    }

    /**
     * gets current day of Advent calendar.
     *
     * @return the current day starting from 0 (day before December 1st)
     */
    public int getDay() {
        return currentDay;
    }

    /**
     * Method that increments the current day by one if possible.
     * if the current day is the final day of the calendar, it is not incremented
     *
     * @return true if day was successfully incremented, false if not
     */
    public boolean nextDay() {
        if (currentDay < finalDay) {
            currentDay++;
            return true;

        }
        return false;
    }

    /**
     * Increments currentDay by number of days specified.
     * If the parameter days is smaller than one, currentDay is not incremented
     * If current days would be bigger than the final day of the calendar after the
     * incrementation, current day is not incremented
     *
     * @param days number of days which the current day should be incremented by
     * @return true if current day was successfully incremented, false if not
     */
    public boolean nextDays(int days) {
        if (days < 1) {
            return false;
        } else if (currentDay + days <= finalDay) {
            currentDay += days;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that checks if a door is opened.
     * If the specified door number is smaller than one or bigger than the
     * final day, the return value is false
     *
     * @param number door number that should be checked (starting from one for 1st December)
     * @return true if door is opened, false if closed or if the value is not valid
     */
    public boolean isDoorOpen(int number) {
        if ((number < 1) || (number > finalDay)) {
            return false;
        }
        return doors.get(number - 1);
    }

    /**
     * Method that opens a door and returns the contained Candy.
     * If the door number is bigger than the current Day or smaller than one, null is returned
     * If door has already been opened, null is returned
     *
     * @param number number of door that should be opened
     * @return Candy that was contained in the door, or null if door not available
     */
    public Candy openDoor(int number) {
        if ((number > currentDay) || (number < 1)) {
            return null;
        }
        if (doors.get(number - 1)) {
            return null;
        } else {
            doors.set(number - 1, true);
            return candies.get(number - 1);
        }
    }

    /**
     * Opens multiple doors and retrieves the candies behind them.
     * if a door is already opened or the door number is smaller than one or
     * bigger than the current day, it is skipped
     *
     * @param numbers list of door numbers to be opened
     * @return list of candies that was retrieved from the opened doors
     */
    public List<Candy> openDoors(List<Integer> numbers) {
        List<Candy> removedCandy = new ArrayList<>();

        for (int number : numbers) {
            if ((number < 1) || (number > currentDay)) {
                continue;
            }

            if (doors.get(number - 1)) {
                continue;
            }

            removedCandy.add(candies.get(number - 1));
            doors.set(number - 1, true);
        }

        return removedCandy;
    }

    /**
     * Method that returns the number of doors that have yet to be opened at the current day.
     *
     * @return number of doors that are still unopened
     */
    public int numberOfUnopenedDoors() {
        int unopenedDoors = 0;
        for (int i = 0; i < currentDay; i++) {
            if (!doors.get(i)) {
                unopenedDoors += 1;
            }
        }
        return unopenedDoors;
    }

    /**
     * Method that resets the AdventCalendar.
     * closes all doors and sets current day to 0 (day before December 1st)
     * Resets all the candy amounts to original
     */
    public void reset() {
        for (int i = 0; i < finalDay; i++) {
            doors.set(i, false);
        }

        currentDay = 0;

        //reassigns candies to original state
        candies = deepCopyCandies(candiesBackup);
    }

    /**
     * Returns a string representation of the advent calendar, showing the state of each door and the candies behind them.
     * Empty doors are represented with the String: "[   ]"
     * Unopened doors are represented with the candy quantity and the candy name contained in braces
     *
     * @return A string representing the state of the advent calendar.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String emptyDoor = "   ";
        int count = 1;

        for (int i = 0; i < finalDay; i++) {
            if (doors.get(i)) {
                stringBuilder.append("[%s]".formatted(emptyDoor));
            } else {
                stringBuilder.append("[%dx%s]".formatted(candies.get(i).quantity, candies.get(i).getName()));
            }
            count++;

            //adding linebreak specified amount of doors
            if (count == LINE_BREAK_FREQUENCY + 1) {
                stringBuilder.append(System.lineSeparator());
                count = 1;
            }
        }

        return stringBuilder.toString();

    }
}
