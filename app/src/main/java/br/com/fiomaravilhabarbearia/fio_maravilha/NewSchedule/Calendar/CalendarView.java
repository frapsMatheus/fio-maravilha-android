package br.com.fiomaravilhabarbearia.fio_maravilha.NewSchedule.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import br.com.fiomaravilhabarbearia.fio_maravilha.R;

/**
 * Created by a7med on 28/06/2015.
 */
public class CalendarView extends LinearLayout
{
    // for logging
    private static final String LOGTAG = "Calendar View";

    // how many days to show, defaults to six weeks, 42 days
    private static final int DAYS_COUNT = 35;

    // default date format
    private static final String DATE_FORMAT = "MMM yyyy";

    // date format
    private String dateFormat;

    // current displayed month
    private Calendar currentDate = Calendar.getInstance();

    public Date _selectedDate = new Date(1);

    //event handling
    private EventHandler eventHandler = null;

    // internal components
    private LinearLayout header;
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtMonth;
    private TextView txtYear;
    private GridView grid;

    public void selectDate(Date selectedDate) {
        _selectedDate = selectedDate;
        updateCalendar(null);
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

        updateCalendar();
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
    }

    private void assignClickHandlers()
    {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                currentDate.add(Calendar.MONTH, 1);
                updateCalendar();
            }
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

            eventHandler.onDayLongPress((Date)view.getItemAtPosition(position));
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

        public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Date> eventDays)
        {
            super(context, R.layout.control_calendar_day, days);
            this.eventDays = eventDays;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            // day in question
            Date date = getItem(position);
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            // today
            Date today = new Date();

            //Current selected
            Date currentSelected = currentDate.getTime();

            // inflate item if it does not exist yet
            if (view == null)
                view = inflater.inflate(R.layout.control_calendar_day, parent, false);

            // if this day has an event, specify event image
            view.setBackgroundResource(0);
            if (eventDays != null)
            {
                for (Date eventDate : eventDays)
                {
                    if (eventDate.getDate() == day &&
                            eventDate.getMonth() == month &&
                            eventDate.getYear() == year)
                    {
                        // mark this day for event
//                        view.setBackgroundResource(R.drawable.reminder);
                        break;
                    }
                }
            }

            // clear styling
            ((TextView)view).setTypeface(null, Typeface.NORMAL);
            ((TextView)view).setTextColor(Color.BLACK);

            if (month != currentSelected.getMonth() || year != currentSelected.getYear())
            {
                // if this day is outside current month, grey it out
                ((TextView)view).setTextColor(getResources().getColor(R.color.greyed_out));
            }
            else if (day == today.getDate() && currentSelected.getMonth() == today.getMonth())
            {
                // if it is today, set it to orange/bold
                ((TextView)view).setTypeface(null, Typeface.BOLD);
                ((TextView)view).setTextColor(getResources().getColor(R.color.today));
            } else if (day == _selectedDate.getDate() && month == _selectedDate.getMonth()
                    && year == _selectedDate.getYear() ) {
                ((TextView)view).setTypeface(null, Typeface.NORMAL);
                view.setBackgroundResource(R.drawable.calendar_selected_background);
                ((TextView)view).setTextColor(getResources().getColor(R.color.colorLightOrange));
            }

            // set text
            ((TextView)view).setText(String.valueOf(date.getDate()));

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