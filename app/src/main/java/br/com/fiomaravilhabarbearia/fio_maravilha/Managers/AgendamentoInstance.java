package br.com.fiomaravilhabarbearia.fio_maravilha.Managers;

import java.util.ArrayList;

import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Barber;
import br.com.fiomaravilhabarbearia.fio_maravilha.Entities.Service;

/**
 * Created by fraps on 07/02/17.
 */

public class AgendamentoInstance {

    private static AgendamentoInstance _shared;

    public ArrayList<Service> _chosenServices = new ArrayList<>();
    public Barber _chosenBarber;


    public static AgendamentoInstance getInstace() {
        if (_shared == null) {
            _shared = new AgendamentoInstance();
        }
        return _shared;
    }

    public void clean(){
        _chosenServices.clear();
        _chosenBarber = null;
    }

}
