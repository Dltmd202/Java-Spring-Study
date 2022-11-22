import org.junit.jupiter.api.Test;

class DoorTest {

	@Test
	void test() {
		Door door = new Door();
		assertEquals(door.getState().getClass(), Closed.class);
		door.close();
		assertEquals(door.getState().getClass(), Closed.class);
		door.unlock();
		assertEquals(door.getState().getClass(), Closed.class);
		door.open();
		assertEquals(door.getState().getClass(), Opened.class);
		door.lock();
		assertEquals(door.getState().getClass(), Opened.class);
		door.unlock();
		assertEquals(door.getState().getClass(), Opened.class);
		door.close();
		assertEquals(door.getState().getClass(), Closed.class);
		door.lock();
		assertEquals(door.getState().getClass(), Locked.class);
		door.close();
		assertEquals(door.getState().getClass(), Locked.class);
		door.open();
		assertEquals(door.getState().getClass(), Locked.class);
		door.unlock();
		assertEquals(door.getState().getClass(), Closed.class);
		door.open();
		assertEquals(door.getState().getClass(), Opened.class);
		door.close();
		assertEquals(door.getState().getClass(), Closed.class);
	}

}
