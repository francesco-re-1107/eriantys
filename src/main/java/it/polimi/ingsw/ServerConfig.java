package it.polimi.ingsw;

import java.nio.file.Path;

/**
 * This record holds the server configuration.
 * @param port port number for the server
 * @param backupFolder folder where the backups are stored
 */
public record ServerConfig(int port, Path backupFolder) { }
