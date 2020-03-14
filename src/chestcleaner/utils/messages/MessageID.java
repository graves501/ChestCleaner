package chestcleaner.utils.messages;

public enum MessageID {

	SYTAX_ERROR(0), ERROR(1), PERMISSON_DENIED(2), SORTING_ON_COOLDOWN(3), BLOCK_HAS_NO_INV(4), INVENTORY_SORTED(5),
	INVALID_WORLD_NAME(6), NOT_A_PLAYER(7), TIMER_ACTIVATED(8), TIMER_DEACTIVATED(9), TIMER_NEW_TIME(10),
	NEW_ITEM_NAME(11), NEW_ITEM_LORE(12), NEW_ITEM(13), HOLD_AN_ITEM(14), GOT_ITEM(15), ITEM_ACTIVATED(16),
	ITEM_DEACTIVATED(17), PLAYER_GOT_ITEM(18), PLAYER_IS_NOT_ONLINE(19), SET_INVENTORY_DETECTION_MODE(20),
	NEW_UPDATE_AVAILABLE(21), DURABILITYLOSS_ACTIVATED(22), DURABILITYLOSS_DEACTIVATED(23), SET_TO_BLACKLIST(24),
	REMOVED_FORM_BLACKLIST(25), BLACKLIST_DOESNT_CONTAINS(26), INDEX_OUT_OF_BOUNDS(27), BLACKLIST_IS_EMPTY(28),
	BLACKLIST_TITLE(29), NEXT_ENTRIES(30), INVALID_INPUT_FOR_INTEGER(31), INVALID_PAGE_NUMBER(32),
	NO_MATERIAL_FOUND(33), BLACKLIST_CLEARED(34), IS_ALREADY_ON_BLACKLIST(35), INVENTORY_ON_BLACKLIST(36),
	NO_PATTERN_ID(37), NEW_PATTERN_SET(38), NO_EVALUATOR_ID(39), NEW_EVALUATOR_SET(40), AUTOSORT_WAS_SET(41),
	NEW_DEFAULT_SORTING_PATTERN(42), NEW_DEFAULT_EVALUATOR(43), DEFUALT_AUTOSORT(44);

	int id;

	MessageID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

}
