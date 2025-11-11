package sgeb.dao;


import sgeb.model.Adherent;
import java.util.ArrayList;
import java.util.List;

public class AdherentDAO {
    
    private List<Adherent> listeAdherents = new ArrayList<>();

    public void add(Adherent adherent) {
        listeAdherents.add(adherent);
    }

    public List<Adherent> getlisteAdherents() {
        return listeAdherents;
    }
}

