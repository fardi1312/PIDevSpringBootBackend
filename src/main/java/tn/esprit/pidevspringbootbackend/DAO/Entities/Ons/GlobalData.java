package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import org.springframework.stereotype.Component;

@Component

public class GlobalData {
    private int globalVariable;

    public int getGlobalVariable() {
        return globalVariable;
    }

    public void setGlobalVariable(int globalVariable) {
        this.globalVariable = globalVariable;
    }



}
