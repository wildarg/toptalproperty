package com.homesoftwaretools.toptalproperty.di

import com.homesoftwaretools.toptalproperty.core.navigator.AppNavigator
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.utils.*
import com.homesoftwaretools.toptalproperty.features.dashboard.DashboardActivity
import com.homesoftwaretools.toptalproperty.features.dashboard.DashboardViewModel
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListUseCase
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.ApartmentListViewModel
import com.homesoftwaretools.toptalproperty.features.editor.ApartmentEditorUseCase
import com.homesoftwaretools.toptalproperty.features.editor.ApartmentEditorUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.editor.ApartmentEditorViewModel
import com.homesoftwaretools.toptalproperty.features.filter.FilterViewModel
import com.homesoftwaretools.toptalproperty.features.splash.SplashScreen
import com.homesoftwaretools.toptalproperty.features.splash.SplashUseCase
import com.homesoftwaretools.toptalproperty.features.splash.SplashUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.splash.SplashViewModel
import com.homesoftwaretools.toptalproperty.features.usercard.UserCardUseCase
import com.homesoftwaretools.toptalproperty.features.usercard.UserCardUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.usercard.UserCardViewModel
import com.homesoftwaretools.toptalproperty.features.userlist.UserListUseCase
import com.homesoftwaretools.toptalproperty.features.userlist.UserListUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.userlist.UserListViewModel
import com.homesoftwaretools.toptalproperty.features.welcome.WelcomeActivity
import com.homesoftwaretools.toptalproperty.features.welcome.WelcomeViewModel
import com.homesoftwaretools.toptalproperty.features.welcome.login.LoginUseCase
import com.homesoftwaretools.toptalproperty.features.welcome.login.LoginUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.welcome.login.LoginViewModel
import com.homesoftwaretools.toptalproperty.features.welcome.register.RegisterUseCase
import com.homesoftwaretools.toptalproperty.features.welcome.register.RegisterUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.welcome.register.RegisterViewModel
import com.homesoftwaretools.toptalproperty.features.welcome.signup.SignUpUseCase
import com.homesoftwaretools.toptalproperty.features.welcome.signup.SignUpUseCaseImpl
import com.homesoftwaretools.toptalproperty.features.welcome.signup.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    scope(named("BaseActivity")) {
        scoped<Navigator> { AppNavigator(getSource()) }
        scoped<Toaster> { AndroidToaster(getSource()) }
    }

    single<ResourceProvider> { AndroidResourceProvider(get()) }

    viewModel { (id: String) -> WelcomeViewModel(id) }
    viewModel { (id: String) -> LoginViewModel(id) }
    viewModel { (id: String) -> SplashViewModel(id) }
    viewModel { (id: String) -> UserCardViewModel(id) }
    viewModel { (id: String) -> ApartmentEditorViewModel(id) }
    viewModel { (id: String) -> ApartmentListViewModel(id) }
    viewModel { (id: String) -> DashboardViewModel(id) }
    viewModel { (id: String) -> FilterViewModel(id) }
    viewModel { (id: String) -> SignUpViewModel(id) }
    viewModel { (id: String) -> RegisterViewModel(id) }
    viewModel { (id: String) -> UserListViewModel(id) }
}

val useCases = module {
    factory<LoginUseCase> { LoginUseCaseImpl(authRepo = get(), userRepo = get()) }
    factory<SignUpUseCase> { SignUpUseCaseImpl(authRepo = get()) }
    factory<RegisterUseCase> { RegisterUseCaseImpl(authRepo = get(), userRepo = get()) }
    factory<SplashUseCase> { SplashUseCaseImpl(authRepo = get(), userRepo = get()) }
    factory<UserCardUseCase> { UserCardUseCaseImpl(authRepo = get(), userRepo = get()) }
    factory<UserListUseCase> { UserListUseCaseImpl(userRepo = get()) }
    factory<ApartmentEditorUseCase> {
        ApartmentEditorUseCaseImpl(
            apartmentRepo = get(),
            userRepo = get(),
            geocodeRepo = get(),
            rp = get()
        )
    }
    factory<ApartmentListUseCase> {
        ApartmentListUseCaseImpl(
            apartRepo = get(),
            userRepo = get(),
            filterRepo = get()
        )
    }
}

val utils = module {
    single<DateProvider> { DateProviderImpl() }
    single<NumberFormatter> { NumberFormatterImpl(rp = get()) }
}