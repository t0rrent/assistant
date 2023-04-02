package au.com.cascadesoftware.engine3.desktop.in;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import au.com.cascadesoftware.engine2.math.Vector2i;

public class InputListener implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener{

	private final InputHandlerDesktop inputHandler;
	
	public InputListener(InputHandlerDesktop inputHandler){
		this.inputHandler = inputHandler;
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		inputHandler.mouseDown(e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		inputHandler.mouseUp(e.getButton());
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {
		inputHandler.setMousePos(new Vector2i(-1, -1));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		inputHandler.setMousePos(new Vector2i(e.getX(), e.getY()));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		inputHandler.setMousePos(new Vector2i(e.getX(), e.getY()));
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		inputHandler.keyDown(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		inputHandler.keyUp(e.getKeyCode());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		inputHandler.setScrollValue(e.getUnitsToScroll());
	}
	
}
