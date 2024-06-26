package kz.danik.yel.view.blank;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

import kz.danik.yel.view.main.MainView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Route(value = "blank-view", layout = MainView.class)
@ViewController("yel_BlankView")
@ViewDescriptor("blank-view.xml")
public class BlankView extends StandardView {

    @Subscribe(id = "doSmth", subject = "clickListener")
    public void onDoSmthClick(final ClickEvent<JmixButton> event) throws IOException {
        String pythonScriptPath = "\"C:\\Users\\UCO - T14\\Downloads\\scrapper\\main.py\""; // Replace with the actual path to your Python script
        String pythonExecutable = "python"; // or "python3" depending on your system

        // Parameters to pass to the Python script
        String username = "musiqaxoe";
        String searchTerm = "_seyran_077";

        // Build the command
        List<String> command = new ArrayList<>();
        command.add(pythonExecutable);
        command.add(pythonScriptPath);
        command.add(username);
        command.add(searchTerm);

        // Run the command
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}