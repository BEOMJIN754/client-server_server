import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLogger {
    private static final Logger logger = Logger.getLogger("ServerLogger");
    private static final ServerLogger serverLogger = new ServerLogger();
    private FileHandler logFile;

    private ServerLogger() {
        try {
            // 날짜 형식 설정
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String fileDate = dateFormat.format(new Date());
            String logDir = "logs";
            String logFileName = logDir + "/log_" + fileDate + ".txt";

            // logs 디렉토리가 없으면 생성
            File dir = new File(logDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 로그 파일 핸들러 설정
            logFile = new FileHandler(logFileName, true); // true는 파일을 append 모드로 만듦
            logFile.setFormatter(new SimpleFormatter());
            logger.addHandler(logFile);

            // 기본 로깅 레벨 설정
            logger.setLevel(Level.INFO);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    // Logger 인스턴스 가져오기 위한 메서드
    public static ServerLogger getLogger() {
        return serverLogger;
    }

    // 로그 작성 메서드
    public void log(Level level, String message) {
        logger.log(level, message);
    }
}
