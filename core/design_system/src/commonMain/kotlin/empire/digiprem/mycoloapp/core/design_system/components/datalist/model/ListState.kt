package empire.digiprem.mycoloapp.core.design_system.components.datalist.model

data class ListState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedItem: T? = null,
    val isDetailPanelOpen: Boolean = false,
    val selectedItems: Set<String> = emptySet(),
    val currentPage: Int = 1,
    val itemsPerPage: Int = 10,
    val totalItems: Int = 0,
    val totalPages: Int = 1,
    val sortBy: empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnSort? = null,
    val filters: Map<String, String> = emptyMap(),
    val visibleColumns: Set<String> = emptySet(),
    val activeTab: String = "",
    val tabs: List<empire.digiprem.mycoloapp.core.design_system.components.datalist.model.TabItem> = emptyList(),
)
