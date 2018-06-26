package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Horario;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;
import br.com.fiomaravilhabarbearia.fio_maravilha.FioUtils;

/**
 * Created by fraps on 07/02/17.
 */

public class AgendamentoInstance {

    private static AgendamentoInstance _shared;

    public ArrayList<Service> _chosenServices = new ArrayList<>();
    public Barber _chosenBarber;
    public Date _chosendDate;
    public Horario _chosenHorario;

    public static AgendamentoInstance getInstace() {
        if (_shared == null) {
            _shared = new AgendamentoInstance();
        }
        return _shared;
    }

    public void clean(){
        _chosenServices.clear();
        _chosenBarber = null;
        _chosendDate = null;
        _chosenHorario = null;
    }

    public int calculateIntervals() {
        int result = 0;
        for (Service service : _chosenServices) {
            result += service.duration / 30;
        }
        return result;
    }

    public boolean isHorarioValid(Horario horario) {
        Calendar c = Calendar.getInstance();
        Calendar chosenCal = Calendar.getInstance();
        chosenCal.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        chosenCal.setTime(_chosendDate);
        String[] split = horario.horario.split("/")[1].split(":");
        chosenCal.set(Calendar.HOUR_OF_DAY,Integer.valueOf(split[0]));
        chosenCal.set(Calendar.MINUTE,Integer.valueOf(split[1]));
        if (((chosenCal.getTimeInMillis() - c.getTimeInMillis()) < 3600 * 1000)) {
            return false;
        } else {
            _chosendDate = chosenCal.getTime();
            return true;
        }
    }

    public void createAgendamento(Context context, ParseUser user, SaveCallback saveCallback) {
        ArrayList<String> horarios = generateTimeSlots();
        ParseObject newSchedule = new ParseObject("Schedules");
        newSchedule.put("user",user);
        newSchedule.put("date",_chosendDate);
        newSchedule.put("hours",horarios);
        newSchedule.put("state","Criado");
        ArrayList<String> services = new ArrayList<>();
        for (Service service : _chosenServices) {
            services.add(service.name);
        }
        newSchedule.put("services",services);
        newSchedule.put("barber",ParseObject.createWithoutData("Barbers",_chosenBarber.id));
        newSchedule.saveInBackground(e -> {
            if (e == null) {
                Calendar calendar = Calendar.getInstance();
                Calendar chosenCal = Calendar.getInstance();
                chosenCal.setTime(_chosendDate);
                long timeFromNow = chosenCal.getTimeInMillis() - calendar.getTimeInMillis() - 3600 * 1000;
                Random r = new Random();
                int i1 = r.nextInt(99999999);
                FioUtils.scheduleNotification(context, timeFromNow, i1);
            }
            saveCallback.done(e);
        });
    }

    public ArrayList<String> generateTimeSlots() {
        ArrayList<String> novosHorarios = new ArrayList<>();
        int duracao = calculateIntervals() - 1;
        String[] diaSplit = _chosenHorario.horario.split("/");
        int dia = Integer.valueOf(diaSplit[0]);
        String[] hourMinute = diaSplit[1].split(":");
        int horaAtual = Integer.valueOf(hourMinute[0]);
        int minutoAtual = Integer.valueOf(hourMinute[1]);
        String firstPart;

        if (horaAtual<10) {
            firstPart = "/0";
        } else {
            firstPart = "/";
        }
        while (!(duracao == 0)) {
            if (minutoAtual == 0) {
                novosHorarios.add(dia + firstPart + horaAtual + ":0" + minutoAtual);
            } else {
                novosHorarios.add(dia + firstPart + horaAtual + ":" + minutoAtual);
            }
            if (minutoAtual == 30) {
                minutoAtual = 0;
                horaAtual++;
            } else {
                minutoAtual = 30;
            }
            if (horaAtual<10) {
                firstPart = "/0";
            } else {
                firstPart = "/";
            }
            duracao--;
        }
        if (minutoAtual == 0) {
            novosHorarios.add(dia + firstPart + horaAtual + ":0" + minutoAtual);
        } else {
            novosHorarios.add(dia + firstPart + horaAtual + ":" + minutoAtual);
        }
        return novosHorarios;
    }

}
