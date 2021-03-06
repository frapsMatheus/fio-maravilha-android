package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;
import br.com.fiomaravilhabarbearia.fio_maravilha.R;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout
{
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 42;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance(TimeZone.getTimeZone("America/Recife"));

    public Date _selectedDate = new Date(1);
    public boolean dateWasSelected = false;

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout calendarView;
    private ArrayList<TextView> daysViews = new ArrayList<>();
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtMonth;
    private TextView txtYear;
    private GridView grid;

    public void selectDate(Date selectedDate) {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("America/Recife"));
        today.set(Calendar.HOUR_OF_DAY, 3);
        Calendar selectedDay = Calendar.getInstance(TimeZone.getTimeZone("America/Recife"));
        selectedDay.setTime(selectedDate);
        int month = selectedDay.get(Calendar.MONTH);
        int year = selectedDay.get(Calendar.YEAR);
        int dayName = selectedDay.get(Calendar.DAY_OF_WEEK);
        //          Outside current Month
        if (month != currentDate.get(Calendar.MONTH) || year != currentDate.get(Calendar.YEAR) || dayName == 1) {
        }
        // Before today
        else if (selectedDay.getTimeInMillis() < today.getTimeInMillis()) {
        } else {
            _selectedDate = selectedDate;
            dateWasSelected = true;
            updateCalendar(null);
        }
    }

    public CalendarView(Context context)
    {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context, AttributeSet attrs)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.control_calendar, this);

        assignUiElements();
        assignClickHandlers();
        setDimensions(context);

        updateCalendar();
    }

    private void setDimensions(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        if (height < 800) {
            ViewGroup.LayoutParams params = calendarView.getLayoutParams();
            params.height = 240;
            calendarView.setLayoutParams(params);
            txtYear.setTextSize(13);
            txtMonth.setTextSize(13);
            for (TextView textView : daysViews) {
                textView.setTextSize(11);
            }
        } else if (height < 900) {
            ViewGroup.LayoutParams params = calendarView.getLayoutParams();
            params.height = 440;
            txtYear.setTextSize(18);
            txtMonth.setTextSize(18);
            for (TextView textView : daysViews) {
                textView.setTextSize(18);
            }
        }

    }

    private void assignUiElements()
    {
        // layout is inflated, assign local variables to components
        header = (LinearLayout)findViewById(R.id.calendar_header);
        btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
        btnNext = (ImageView)findViewById(R.id.calendar_next_button);
        txtMonth = (TextView)findViewById(R.id.calendar_month_display);
        txtYear = (TextView)findViewById(R.id.calendar_year_display);
        grid = (GridView)findViewById(R.id.calendar_grid);
        calendarView = (LinearLayout)findViewById(R.id.calendar_view);
        daysViews.add((TextView)findViewById(R.id.calendar_day1));
        daysViews.add((TextView)findViewById(R.id.calendar_day2));
        daysViews.add((TextView)findViewById(R.id.calendar_day3));
        daysViews.add((TextView)findViewById(R.id.calendar_day4));
        daysViews.add((TextView)findViewById(R.id.calendar_day5));
        daysViews.add((TextView)findViewById(R.id.calendar_day6));
        daysViews.add((TextView)findViewById(R.id.calendar_day7));
    }

    private void assignClickHandlers()
    {
        // add one month and refresh UI
        btnNext.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(v -> {
            currentDate.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        grid.setOnItemClickListener((parent, view, position, id) ->
                eventHandler.onDayLongPress((Date)parent.getItemAtPosition(position)));

        // long-pressing a day
        grid.setOnItemLongClickListener((view, cell, position, id) -> {
            // handle long-press
            if (eventHandler == null)
                return false;
            eventHandler.onDayLongPress((Date) view.getItemAtPosition(position));
            return true;
        });
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar()
    {
        updateCalendar(null);
    }

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(HashSet<Date> events)
    {
        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar)currentDate.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        grid.setAdapter(new CalendarAdapter(getContext(), cells, events));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
        txtMonth.setText(sdf.format(currentDate.getTime()).toUpperCase());
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
        txtYear.setText(sdf2.format(currentDate.getTime()));
    }


    private class CalendarAdapter extends ArrayAdapter<Date>
    {
        // days with events
        private HashSet<Date> eventDays;

        // for view inflation
        private LayoutInflater inflater;

        private final Context context;

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            // day in question
            Calendar date = Calendar.getInstance();
            date.setTime(getItem(position));
            int day = date.get(Calendar.DAY_OF_MONTH);
            int dayName = date.get(Calendar.DAY_OF_WEEK);
            int month = date.get(Calendar.MONTH);
            int year = date.get(Calendar.YEAR);

            // today
            Calendar today = Calendar.getInstance();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            // if this day has an event, specify event image
            view.setBackgroundResource(0);

            // clear styling
            ((TextView)view).setTypeface(null, Typeface.NORMAL);
            ((TextView)view).setTextColor(Color.BLACK);

            Calendar selectedDate = Calendar.getInstance();
            selectedDate.setTime(_selectedDate);

            // Selected day
            if (day == selectedDate.get(Calendar.DAY_OF_MONTH) && month == selectedDate.get(Calendar.MONTH)
                    && year == selectedDate.get(Calendar.YEAR) ) {
                ((TextView)view).setTypeface(null, Typeface.NORMAL);
                view.setBackgroundResource(R.drawable.calendar_selected_background);
                ((TextView)view).setTextColor(FioUtils.getColor(parent.getContext(),R.color.colorLightOrange));
            }
            // Today
            else  if (day == today.get(Calendar.DAY_OF_MONTH) && currentDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
            {
                // if it is today, set it to orange/bold
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                ((TextView)view).setTextColor(FioUtils.getColor(parent.getContext(),R.color.today));
            }
//          Outside current Month
            else if (month != currentDate.get(Calendar.MONTH) || year != currentDate.get(Calendar.YEAR) || dayName == 1) {
                ((TextView) view).setTextColor(FioUtils.getColor(parent.getContext(), R.color.greyed_out));
            }
            // Before today
            else if (date.getTimeInMillis() < today.getTimeInMillis()) {
                ((TextView) view).setTextColor(FioUtils.getColor(parent.getContext(), R.color.greyed_out));
            }

            // set text
            ((TextView)view).setText(String.valueOf(date.get(Calendar.DAY_OF_MONTH)));
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            if (height < 800) {
                ((TextView)view).setTextSize(13);
            } else if (height < 900) {
                ((TextView)view).setTextSize(18);
            }

            return view;
        }
    }

    /**
     * Assign event handler to be passed needed events
     */
    public void setEventHandler(EventHandler eventHandler)
    {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported to
     * the outside world
     */
    public interface EventHandler
    {
        void onDayLongPress(Date date);
    }
}