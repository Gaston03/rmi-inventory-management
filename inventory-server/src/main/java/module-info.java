module ma.supmti.inventoryServer {
    requires java.rmi;
    requires java.sql;

    exports ma.supmti.services;
    exports ma.supmti.models;
    exports ma.supmti.dtos;
}