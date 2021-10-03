package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Stack;
import java.util.TimeZone;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Horario;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Schedule;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioAnalytics;

/**
 * Created by fraps on 08/02/17.
 */

public class Horarios extends Observable {

    public ArrayList<Horario> _horarios = new ArrayList<>();


    private static String[] POSSIBLE_HOURS = {
            "08:00", "08:10", "08:20", "08:30", "08:40", "08:50",
            "09:00", "09:10", "09:20", "09:30", "09:40", "09:50",
            "10:00", "10:10", "10:20", "10:30", "10:40", "10:50",
            "11:00", "11:10", "11:20", "11:30", "11:40", "11:50",
            "12:00", "12:10", "12:20", "12:30", "12:40", "12:50",
            "13:00", "13:10", "13:20", "13:30", "13:40", "13:50",
            "14:00", "14:10", "14:20", "14:30", "14:40", "14:50",
            "15:00", "15:10", "15:20", "15:30", "15:40", "15:50",
            "16:00", "16:10", "16:20", "16:30", "16:40", "16:50",
            "17:00", "17:10", "17:20", "17:30", "17:40", "17:50",
            "18:00", "18:10", "18:20", "18:30", "18:40", "18:50",
            "19:00", "19:10", "19:20", "19:30", "19:40", "19:50",
            "20:00" };

    private static String[] APP_POSSIBLE_HOURS = {
            "09:00", "09:40",
            "10:20",
            "11:00", "11:40",
            "12:20",
            "13:00", "13:40",
            "14:20",
            "15:00", "15:40",
            "16:20",
            "17:00", "17:40",
            "18:20",
            "19:00", "19:40",
            "20:20",
    };

    private static Horarios _shared;
    boolean downloaded = false;

    public static Horarios getInstace() {
        if (_shared == null) {
            _shared = new Horarios();
        }
        return _shared;
    }

    public void kill() {
        _shared = null;
    }

    public void getHorarios(Barber barber, Date date, Handler.Callback callback) {
        ParseQuery query = new ParseQuery("Schedules");
        query.whereContainedIn("state", Arrays.asList("Criado", "Fechado", "Chegou"));
        Calendar initialTime = Calendar.getInstance(TimeZone.getTimeZone("America/Recife"));
        initialTime.setTime(date);
        initialTime.set(Calendar.HOUR_OF_DAY, 0);
        Date initialDate = initialTime.getTime();

        Calendar finalTime = Calendar.getInstance(TimeZone.getTimeZone("America/Recife"));
        finalTime.setTime(date);
        finalTime.set(Calendar.HOUR_OF_DAY, 20);
        Date finalDate = finalTime.getTime();
        query.whereGreaterThanOrEqualTo("date", initialDate);
        query.whereLessThanOrEqualTo("date", finalDate);
        query.whereEqualTo("barber", ParseObject.createWithoutData("Barbers", barber.id));
        query.include("barber");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ArrayList<Schedule> schedules = new ArrayList<>();
                    for (ParseObject object : objects) {
                        schedules.add(new Schedule(object));
                    }
                    generateEmptyHorarios(barber, date, schedules);
                    callback.handleMessage(new Message());
                    FioAnalytics.logSimpleEvent("Baixou horários");
                } else {
                    FioAnalytics.logError("Horários", e.getLocalizedMessage(), e);
                }
            }
        });
    }

    private void generateEmptyHorarios(Barber barber, Date date, ArrayList<Schedule> schedules) {
        ArrayList<Horario> horarios = cleanByDate(date, barber);
        ArrayList<Horario> currentDateHorarios = cleanCurrentDate(horarios, date);
        ArrayList<Horario> emptyHorarios;
        if (schedules != null) {
            emptyHorarios = cleanBusyHours(currentDateHorarios, schedules);
        } else {
            emptyHorarios = currentDateHorarios;
        }
        _horarios = cleanPossibleHours(emptyHorarios,
                AgendamentoInstance.getInstace().calculateIntervals());
    }

    private ArrayList<Horario> cleanByDate(Date date, Barber barber) {
        String currentDate = String.valueOf(date.getDay() + 1);
        ArrayList<Horario> result = new ArrayList<>();
        for (Horario horario : barber.horarios) {
            String[] separated = horario.horario.split("/");
            if (separated[0].equalsIgnoreCase(currentDate)) {
                result.add(horario);
            }
        }
        return result;
    }

    private ArrayList<Horario> cleanBusyHours(ArrayList<Horario> horarios, ArrayList<Schedule> schedules) {
        ArrayList<Horario> result = new ArrayList<>();
        for (Horario horario : horarios) {
            boolean flagHorario = false;
            for (Schedule schedule : schedules) {
                for (Horario busyHorario : schedule.horarios) {
                    if (busyHorario.horario.equalsIgnoreCase(horario.horario)) {
                        flagHorario = true;
                        break;
                    }
                }
                if (flagHorario) {
                    break;
                }
            }
            if (!flagHorario) {
                result.add(horario);
            }
        }
        return result;
    }

    private ArrayList<Horario> cleanCurrentDate(ArrayList<Horario> horarios, Date selectedDate) {
        Calendar selectedCal = Calendar.getInstance(TimeZone.getTimeZone("America/Recife"));
        Calendar currentCal = Calendar.getInstance(TimeZone.getTimeZone("America/Recife"));
        selectedCal.setTime(selectedDate);
        if (selectedCal.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                selectedCal.get(Calendar.DAY_OF_YEAR) == currentCal.get(Calendar.DAY_OF_YEAR)) {
            ArrayList<Horario> result = new ArrayList<>();
            int currentHour = currentCal.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentCal.get(Calendar.MINUTE);
            for (Horario horario : horarios) {
                String[] hourMinute = horario.horario.split("/")[1].split(":");
                int selectedHour = Integer.valueOf(hourMinute[0]);
                int selectedMinute = Integer.valueOf(hourMinute[1]);
                if (selectedHour > currentHour || (selectedHour == currentHour && selectedMinute >= currentMinute)) {
                    result.add(horario);
                }
            }
            return result;
        } else {
            return horarios;
        }

    }

    private ArrayList<Horario> cleanPossibleHours(ArrayList<Horario> horarios, int usedSlots) {
        ArrayList<Horario> result = new ArrayList<>();
        if (horarios.size() > 0) {
            int currentInterval = 0;
            int lastIndex = -1;
            // eslint-disable-next-line no-restricted-syntax
            for (int i = 0; i < horarios.size(); i += 1) {
                Horario horario = horarios.get(i);
                String day = horario.horario.split("/")[0];
                String hora = horario.horario.split("/")[1];
                if (lastIndex == -1) {
                    lastIndex = Arrays.binarySearch(POSSIBLE_HOURS, hora);
                    currentInterval = 1;
                } else {
                    int currentIndex = Arrays.binarySearch(POSSIBLE_HOURS, hora);
                    if (currentIndex - lastIndex != 1) {
                        currentInterval = 1;
                    } else if (currentInterval < usedSlots) {
                        currentInterval += 1;
                    }
                    lastIndex = currentIndex;
                    if (currentInterval == usedSlots) {
                        String novaHora = POSSIBLE_HOURS[currentIndex - usedSlots + 1];
                        Log.d("Hora", novaHora);
                        if (Arrays.binarySearch(APP_POSSIBLE_HOURS, novaHora) > -1) {
                            result.add(new Horario(day + "/" + novaHora));
                        }
                    }
                }
            }
            return result;
        }
        return horarios;
    }
}
