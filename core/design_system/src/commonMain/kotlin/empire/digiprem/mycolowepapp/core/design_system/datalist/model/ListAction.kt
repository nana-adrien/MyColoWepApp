package empire.digiprem.mycolowepapp.core.design_system.datalist.model

sealed interface ListAction<out T> {
    data class View<T>(val item: T) : ListAction<T>
    data class Edit<T>(val item: T) : ListAction<T>
    data class Delete<T>(val item: T) : ListAction<T>
    data class SelectItem<T>(val item: T) : ListAction<T>
    data class UnselectItem<T>(val item: T) : ListAction<T>
    data class SelectAll<T>(val unit: Unit = Unit) : ListAction<T>
    data class UnselectAll<T>(val unit: Unit = Unit) : ListAction<T>
    data class Export<T>(val format: ExportFormat) : ListAction<T>
    data class Import<T>(val format: ImportFormat) : ListAction<T>
    data class SortBy<T>(val sort: ColumnSort) : ListAction<T>
    data class FilterBy<T>(val key: String, val value: String) : ListAction<T>
    data class ToggleColumn<T>(val key: String) : ListAction<T>
    data class ChangePage<T>(val page: Int) : ListAction<T>
    data class ChangePageSize<T>(val size: Int) : ListAction<T>
    data class CloseDetail<T>(val unit: Unit = Unit) : ListAction<T>
    data class New<T>(val unit: Unit = Unit) : ListAction<T>
    data class ChangeTab<T>(val tabId: String) : ListAction<T>
}
