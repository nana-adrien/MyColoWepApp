package empire.digiprem.mycoloapp.core.design_system.components.datalist.model

sealed interface ListAction<out T> {
    data class View<T>(val item: T) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class Edit<T>(val item: T) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class Delete<T>(val item: T) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class SelectItem<T>(val item: T) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class UnselectItem<T>(val item: T) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class SelectAll<T>(val unit: Unit = Unit) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class UnselectAll<T>(val unit: Unit = Unit) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class Export<T>(val format: empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ExportFormat) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class Import<T>(val format: empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ImportFormat) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class SortBy<T>(val sort: empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ColumnSort) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class FilterBy<T>(val key: String, val value: String) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class ToggleColumn<T>(val key: String) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class ChangePage<T>(val page: Int) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class ChangePageSize<T>(val size: Int) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class CloseDetail<T>(val unit: Unit = Unit) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class New<T>(val unit: Unit = Unit) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
    data class ChangeTab<T>(val tabId: String) :
        empire.digiprem.mycoloapp.core.design_system.components.datalist.model.ListAction<T>
}
