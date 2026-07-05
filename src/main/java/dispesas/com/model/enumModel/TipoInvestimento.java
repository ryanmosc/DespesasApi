package dispesas.com.model.enumModel;

public enum TipoInvestimento {

    // Atrelados ao CDI — bot calcula via percentualCdi
    CDB,
    LCI,
    LCA,
    CRI,
    CRA,
    DEBENTURE,
    POUPANCA,
    FUNDOS_DI,
    COE,

    // Atrelados à Selic — bot calcula de forma aproximada via percentualCdi
    TESOURO_SELIC,

    OUTROS
}