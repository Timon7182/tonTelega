package kz.danik.yel.app;

import io.jmix.core.impl.DataManagerImpl;
import io.jmix.core.security.Authenticated;
import kz.danik.yel.entity.TaskStatus;
import kz.danik.yel.entity.TaskType;
import kz.danik.yel.entity.TelegramTask;
import kz.danik.yel.entity.TelegramUserTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component("yel_InstagramService")
public class InstagramService {

    private static final String TO_PAY = "Спасибо за выполнение. Вам скоро выплатят выигрыш!)";
    private static final String NEED_INSTAGRAM = "Отправьте инстаграм для проверки,Формат @username";
    private static final String NO = "Вы не выполнели задание, попробуйте еще раз";
    @Autowired
    private DataManagerImpl core_DataManager;

    @Authenticated
    public String checkTaskForComplete(TelegramUserTask telegramUserTask) throws Exception {
        if(telegramUserTask.getStatus().equals(TaskStatus.DONE)){
            return "TO_PAY";
        }
        TelegramTask task =telegramUserTask.getTask();
        String instagram =telegramUserTask.getUser().getInstagram();

        if(instagram == null || instagram.isEmpty()){
            return "NEED_INSTAGRAM";
        }
        if(task.getType().equals(TaskType.FOLLOW)){
            Boolean isUserFollowsAccount = isUserFollowsAccount(task.getTaskUrl(),instagram);
            if(isUserFollowsAccount){
                return "TO_PAY";
            }
            return "NO";
        }

        return null;
    }

    private Boolean isUserFollowsAccount(String targetInstagramUrl, String userInstagram) throws Exception {
        String pythonScriptPath = "\"C:\\Users\\UCO - T14\\Downloads\\scrapper\\main.py\""; // Replace with the actual path to your Python script
        String pythonExecutable = "python"; // or "python3" depending on your system

        // Parameters to pass to the Python script
        String username = getNickFromUrl(targetInstagramUrl);
        String searchTerm = userInstagram;

        // Build the command
        List<String> command = new ArrayList<>();
        command.add(pythonExecutable);
        command.add(pythonScriptPath);
        command.add(username);
        command.add(searchTerm);

        // Run the command
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        String followers="";
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Number of followers:")) {
                     followers = line.split("Number of followers: ")[1].trim();
                    System.out.println(followers);
                    break;
                }
            }
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.parseInt(followers) != 0;

    }

    public String getNickFromUrl(String urlString) throws Exception {
        try{
            URL url = new URL(urlString);
            String path = url.getPath();

            String nickname = path.replaceAll("^/|/$", "");
            return nickname;
        }catch (Exception exception){
            log.error(ExceptionUtils.getMessage(exception) +
                    " " + ExceptionUtils.getStackTrace(exception));
            throw new Exception("Введена неверная ссылка ");
        }
    }

    public  String getUsernameAfterAtSymbol(String str) {
        int atIndex = str.indexOf('@');
        if (atIndex != -1) {
            return str.substring(atIndex + 1);
        }
        return ""; // or you can return null or throw an exception based on your requirement
    }
}