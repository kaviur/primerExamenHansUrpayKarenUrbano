package service;

import dao.iDao;
import model.Odontologo;

import java.util.List;

public class OdontologoService {

    private iDao<Odontologo> odontologoDao;

    public OdontologoService(iDao<Odontologo> odontologoDao){
        this.odontologoDao = odontologoDao;
    }

    public Odontologo guardarOdontologo(Odontologo odontologo){
        return odontologoDao.guardar(odontologo);
    }

    public Odontologo buscarOdontologo(Integer id){
        return odontologoDao.buscarPorId(id);
    }

    public List<Odontologo> buscarTodos(){
        return odontologoDao.buscarTodos();
    }

    public void eliminarOdontologo(Integer id){
        odontologoDao.eliminar(id);
    }
}