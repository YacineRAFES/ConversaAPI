package fr.afpa.dev.pompey.conversaapi.dao;

import fr.afpa.dev.pompey.conversaapi.modele.Amis;

import java.util.List;

public class AmisDAO extends DAO<Amis> {

    /**
     * @param obj 
     * @return
     */
    @Override
    public int create(Amis obj) {
        return 0;
    }

    /**
     * @param obj 
     * @return
     */
    @Override
    public boolean delete(Amis obj) {
        return false;
    }

    /**
     * @param obj 
     * @return
     */
    @Override
    public boolean update(Amis obj) {
        return false;
    }

    /**
     * @param id 
     * @return
     */
    @Override
    public Amis find(int id) {
        return null;
    }

    /**
     * @return 
     */
    @Override
    public List<Amis> findAll() {
        return List.of();
    }
}
