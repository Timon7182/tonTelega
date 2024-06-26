package kz.danik.yel.view.settings;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import kz.danik.yel.entity.Settings;
import kz.danik.yel.view.main.MainView;

@Route(value = "settingses", layout = MainView.class)
@ViewController("yel_Settings.list")
@ViewDescriptor("settings-list-view.xml")
@LookupComponent("settingsesDataGrid")
@DialogMode(width = "64em")
public class SettingsListView extends StandardListView<Settings> {
}