package kz.danik.yel.view.settings;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import kz.danik.yel.entity.Settings;
import kz.danik.yel.view.main.MainView;

@Route(value = "settingses/:id", layout = MainView.class)
@ViewController("yel_Settings.detail")
@ViewDescriptor("settings-detail-view.xml")
@EditedEntityContainer("settingsDc")
public class SettingsDetailView extends StandardDetailView<Settings> {
}