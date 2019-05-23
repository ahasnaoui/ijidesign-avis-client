package com.orange.oab.lmde.liferay.comparateuroffres.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.orange.oab.lmde.liferay.comparateuroffres.front.OffreFO;

public class OffreForm implements Serializable {

    private static final long serialVersionUID = -413677266961670257L;

    private List<OffreFO> offre = new ArrayList<OffreFO>();

    private String lienGammeDetail;

    /**
     * @return the offre
     */
    public List<OffreFO> getOffre() {
        return offre;
    }

    /**
     * @param offre the offre to set
     */
    public void setOffre(List<OffreFO> offre) {
        this.offre = offre;
    }

    /**
     * @return the lienGammeDetail
     */
    public String getLienGammeDetail() {
        return lienGammeDetail;
    }

    /**
     * @param lienGammeDetail the lienGammeDetail to set
     */
    public void setLienGammeDetail(String lienGammeDetail) {
        this.lienGammeDetail = lienGammeDetail;
    }

}
