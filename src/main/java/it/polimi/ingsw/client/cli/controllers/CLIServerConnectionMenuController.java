package it.polimi.ingsw.client.cli.controllers;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.ScreenController;
import it.polimi.ingsw.client.cli.Cursor;
import it.polimi.ingsw.client.cli.views.ProgressIndicatorView;
import it.polimi.ingsw.client.cli.views.SimpleInputView;
import it.polimi.ingsw.client.cli.views.TitleView;
import it.polimi.ingsw.common.exceptions.DuplicatedNicknameException;

/**
 * This class is responsible for controlling the server connection menu of the CLI.
 */
public class CLIServerConnectionMenuController implements ScreenController {

    /**
     * Input view for the connection params aka the server address and port.
     */
    private SimpleInputView connectionParamsView;

    /**
     * Input view for the nickname.
     */
    private SimpleInputView nicknameView;

    private ProgressIndicatorView connectionProgressIndicator;

    @Override
    public void onShow() {
        Cursor.getInstance().clearScreen();

        new TitleView(TitleView.Title.ERIANTYS).draw();

        connectionProgressIndicator = new ProgressIndicatorView(Cursor.WIDTH - 1, 22);

        connectionParamsView = new SimpleInputView("Inserisci hostname:port [default localhost:6001]:");
        connectionParamsView.setListener(this::processConnectionParams);
        connectionParamsView.draw();

        nicknameView = new SimpleInputView("Inserisci nickname [default casuale]: ");
        nicknameView.setListener(this::processNickname);
    }

    /**
     * Called when the user inputs the connection params.
     * @param conf the connection params string
     */
    private void processConnectionParams(String conf) {
        var ip = "";
        var port = "";
        if(!conf.isEmpty()) {
            try {
                ip = conf.split(":")[0];
                port = conf.split(":")[1];
            } catch (Exception e) {
                connectionParamsView.showError("Formato non valido, usa hostname:port");
                return;
            }
        }

        connectionProgressIndicator.startLoading();
        Client.getInstance().connect(
                ip,
                port,
                () -> {
                    connectionProgressIndicator.stopLoading();
                    askNickname();
                },
                e -> {
                    connectionProgressIndicator.stopLoading();
                    connectionParamsView.showError("Impossibile connettersi al server");
                }
        );
    }

    /**
     * Called when the user inputs the nickname.
     * @param nickname the nickname string
     */
    private void processNickname(String nickname) {
        Client.getInstance().registerNickname(
                nickname,
                e -> {
                    if(e instanceof DuplicatedNicknameException)
                        nicknameView.showError("Nickname già presente sul server");
                    else
                        nicknameView.showError("Nickname non valido");
                }
        );
    }

    /**
     * Called after the client connects to the server.
     */
    private void askNickname() {
        nicknameView.draw();
    }

    @Override
    public void onHide() {
        //nothing to do
    }

}
