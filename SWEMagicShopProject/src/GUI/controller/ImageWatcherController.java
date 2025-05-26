package GUI.controller;
import java.nio.file.*;
import java.io.IOException;
import javafx.application.Platform;

//TODO: CONTROLLA SE QUESTA CLASSE è INUTILE
public class ImageWatcherController {
    private static final String IMAGE_FOLDER = "SWEMagicShopProject/src/images/products"; // Percorso della cartella

    public static void watchFolder(Runnable updateCallback) {
        new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(IMAGE_FOLDER);

                // Registra la cartella per eventi di creazione, modifica ed eliminazione
                path.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take(); // Aspetta un evento
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Platform.runLater(updateCallback); // Aggiorna la GUI
                    }
                    key.reset(); // Resetta la chiave per continuare a monitorare
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
