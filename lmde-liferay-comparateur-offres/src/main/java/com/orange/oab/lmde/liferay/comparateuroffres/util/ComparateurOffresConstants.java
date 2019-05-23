package com.orange.oab.lmde.liferay.comparateuroffres.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Constantes du portlet COMPARATEUR D'OFFRES
 * 
 * @author Florian FRANCHETEAU (flmc8433) <florian.francheteau@orange.com>
 */
public final class ComparateurOffresConstants {

    /**
     * Constructeur privé car classe utilitaire.
     */
    private ComparateurOffresConstants() {
        super();
    }

    static {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#0.00");
        DECIMAL_FORMAT_FRANCE = df;
    }

    public static final String VIEW = "view";

    public static final String EDIT = "edit";

    public static final String GRAPHIQUE = "modeGraphique";

    public static final String FULL_GRAPHIQUE = "modeGraphiqueFull";

    public static final String LISTE = "listeOffres";

    public static final String TEXTE = "modeTextuel";

    public static final String STATUT_GAMME = "OUVERT";

    public static final String PREFERENCE_CODE_GAMME = "codeGamme";

    public static DecimalFormat DECIMAL_FORMAT_FRANCE;

}
