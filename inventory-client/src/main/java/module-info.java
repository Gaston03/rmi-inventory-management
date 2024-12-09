module ma.supmti.inventoryclient {
    requires java.rmi;
    requires java.sql;

    requires ma.supmti.inventoryServer;

    exports ma.supmti.inventoryclient;
}