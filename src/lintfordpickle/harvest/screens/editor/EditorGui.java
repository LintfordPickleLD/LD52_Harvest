package lintfordpickle.harvest.screens.editor;

import lintfordpickle.harvest.screens.editor.panels.LayerAnimationPanel;
import lintfordpickle.harvest.screens.editor.panels.LayerNoisePanel;
import lintfordpickle.harvest.screens.editor.panels.LayerPhysicsPanel;
import lintfordpickle.harvest.screens.editor.panels.LayerTexturePanel;
import lintfordpickle.harvest.screens.editor.panels.LayersPanel;
import lintfordpickle.harvest.screens.editor.panels.ScenePanel;
import net.lintfordLib.editor.renderers.UiDockedWindow;
import net.lintfordLib.editor.renderers.panels.CameraPanel;
import net.lintfordLib.editor.renderers.panels.CursorPanel;
import net.lintfordLib.editor.renderers.panels.FileInfoPanel;
import net.lintfordLib.editor.renderers.panels.GridPanel;
import net.lintfordlib.core.LintfordCore;
import net.lintfordlib.renderers.RendererManager;
import net.lintfordlib.renderers.windows.components.UiLabel;

public class EditorGui extends UiDockedWindow {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String GUI_NAME = "Editor";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private UiLabel mWindowTitle;

	// --------------------------------------
	// Properties
	// --------------------------------------

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public EditorGui(RendererManager rendererManager, int entityGroupUid) {
		super(rendererManager, GUI_NAME, entityGroupUid);

		UiDockedWindow.DOCKED_WINDOW_WIDTH = 320;

		mWindowTitle = new UiLabel(this, "Window Title");

		addComponent(mWindowTitle);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void update(LintfordCore core) {
		super.update(core);

		final var lHudBoundBox = core.HUD().boundingRectangle();
		mWindowTitle.set(lHudBoundBox.left(), lHudBoundBox.top(), lHudBoundBox.width(), 35);
	}

	// --------------------------------------
	// Methods
	// --------------------------------------

	@Override
	protected void createGuiPanels() {
		super.createGuiPanels();

		editorPanels().add(new FileInfoPanel(this, mEntityGroupUid));
		editorPanels().add(new ScenePanel(this, mEntityGroupUid));
		editorPanels().add(new CameraPanel(this, mEntityGroupUid));
		editorPanels().add(new CursorPanel(this, mEntityGroupUid));
		editorPanels().add(new GridPanel(this, mEntityGroupUid));
		editorPanels().add(new LayersPanel(this, mEntityGroupUid));

		editorPanels().add(new LayerTexturePanel(this, mEntityGroupUid));
		editorPanels().add(new LayerAnimationPanel(this, mEntityGroupUid));
		editorPanels().add(new LayerNoisePanel(this, mEntityGroupUid));
		editorPanels().add(new LayerPhysicsPanel(this, mEntityGroupUid));
	}

}
