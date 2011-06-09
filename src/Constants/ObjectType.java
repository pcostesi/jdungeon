package Constants;

public enum ObjectType {
	PLAYER(1), WALL(2), ENEMY(3), HEALTHBONUS(4), STRENGTHBONUS(5), INVALID(6);

	public final int code;

	private ObjectType(int i) {
		this.code = i;
	}

	public static ObjectType objFromInt(int code) {
		switch (code) {
		case 1:
			return PLAYER;
		case 2:
			return WALL;
		case 3:
			return ENEMY;
		case 4:
			return HEALTHBONUS;
		case 5:
			return STRENGTHBONUS;
		default:
			return INVALID;
		}

	}
}
