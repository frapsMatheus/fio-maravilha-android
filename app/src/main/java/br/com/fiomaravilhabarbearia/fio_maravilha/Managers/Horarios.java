package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import android.os.Handler;
import android.os.Message;

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

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Horario;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Schedule;

/**
 * Created by fraps on 08/02/17.
 */

public class Horarios extends Observable {

    public ArrayList<Horario> _horarios = new ArrayList<>();

    private static Horarios _shared;
    boolean downloaded = false;

    public static Horarios getInstace() {
        if (_shared == null) {
            _shared = new Horarios();
        }
        return _shared;
    }

    public void getHorarios(Barber barber, Date date, Handler.Callback callback) {
        ParseQuery query = new ParseQuery("Schedules");
        query.whereContainedIn("state", Arrays.asList("Criado"));
        Calendar initialTime = Calendar.getInstance();
        initialTime.setTime(date);
        initialTime.set(Calendar.HOUR_OF_DAY, 0);
        Date initialDate = initialTime.getTime();

        Calendar finalTime = Calendar.getInstance();
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
                    generateEmptyHorarios(barber,date,schedules);
                    callback.handleMessage(new Message());
                }
            }
        });
    }

    private void generateEmptyHorarios(Barber barber, Date date, ArrayList<Schedule> schedules) {
        ArrayList<Horario> horarios = cleanByDate(date,barber);
        ArrayList<Horario> emptyHorarios = cleanBusyHours(horarios,schedules);
        _horarios =  cleanPossibleHours(emptyHorarios,
                AgendamentoInstance.getInstace().calculateIntervals());
    }

    private ArrayList<Horario> cleanByDate(Date date, Barber barber) {
        String currentDate = String.valueOf(((date.getDay() + 1) % 7) + 1);
        ArrayList<Horario> result = new ArrayList<>();
        for (Horario horario : barber.horarios) {
            String[] separated = horario.horario.split("/");
            if (separated[0].equalsIgnoreCase(currentDate) ) {
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

    private ArrayList<Horario> cleanPossibleHours(ArrayList<Horario> horarios, int usedSlots) {
        Stack horariosValidos = new Stack();
        int lastHour = 0;
        String lastMinute = "00";
        Horario firstHorario = horarios.remove(0);
        String[] horaMinutos = firstHorario.horario.split("/")[1].split(":");
        lastHour = Integer.valueOf(horaMinutos[0]);
        lastMinute = horaMinutos[1];
        horariosValidos.add(firstHorario);
        for (Horario horario : horarios) {
            String[] horaMinutosNovo = horario.horario.split("/")[1].split(":");
            final int newHour = Integer.valueOf(horaMinutosNovo[0]);
            final String newMinute = horaMinutosNovo[1];
            if  (lastHour == newHour || (lastHour+1 == newHour && lastMinute != newMinute)) {
            } else {
                popStack(horariosValidos,usedSlots);
            }
            horariosValidos.add(horario);
            lastHour = newHour;
            lastMinute = newMinute;
        }
        popStack(horariosValidos,usedSlots);
        return new ArrayList<>(horariosValidos);
    }

    private void popStack(Stack stack, int usedSlots) {
        for (int i=1; i< usedSlots; i++) {
            stack.pop();
        }
    }
}
