package empire.digiprem.mycoloapp.home

sealed interface HomeAction {
    data object OnInitAction : HomeAction
}