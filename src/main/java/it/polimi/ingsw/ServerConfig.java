package it.polimi.ingsw;

/**
 * This record holds the server configuration.
 * @param port port number for the server
 * @param backupFolder folder where the backups are stored
 */
public record ServerConfig(int port, String backupFolder) { }
