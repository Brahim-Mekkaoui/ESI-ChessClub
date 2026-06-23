package ma.ac.esi.chessclub;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        // Chemin du webapp : src/main/webapp en dev, webapp à l'intérieur du JAR
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        if (!new File(webappDir).exists()) {
            // Mode JAR : utiliser les ressources embarquées
            webappDir = Main.class.getClassLoader().getResource("").getPath();
            webappDir = new File(webappDir).getParentFile().getParentFile().getAbsolutePath() + "/webapp";
        }

        Context ctx = tomcat.addWebapp("/chessclub", webappDir);

        tomcat.start();
        System.out.println("  ESIChessClub démarré !");
        System.out.println("  http://localhost:" + port + "/chessclub/");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try { tomcat.stop(); tomcat.destroy(); }
            catch (Exception e) { e.printStackTrace(); }
        }));

        tomcat.getServer().await();
    }
}