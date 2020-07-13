package com.homesoftwaretools.toptalproperty.core.ui

import androidx.lifecycle.ViewModel
import com.homesoftwaretools.toptalproperty.logd
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.scope.ScopeID
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(private val scopeID: ScopeID) : ViewModel(), KoinComponent {
    val BaseViewModel.scope
            get() = getKoin().getScope(scopeID)

    private var disposable = CompositeDisposable()

    fun <T : Any> Single<T>.bindSubscribe(
        onSuccess: (t: T) -> Unit,
        onError: (e: Throwable) -> Unit
    ) {
        subscribe(onSuccess, onError).let { disposable.add(it) }
    }

    fun Completable.bindSubscribe(
        onComplete: () -> Unit,
        onError: (e: Throwable) -> Unit
    ) {
        subscribe(onComplete, onError).let { disposable.add(it) }
    }

    fun <T : Any> Maybe<T>.bindSubscribe(
        onSuccess: (t: T) -> Unit,
        onComplete: () -> Unit,
        onError: (e: Throwable) -> Unit
    ) {
        subscribe(onSuccess, onError, onComplete).let { disposable.add(it) }
    }

    fun <T : Any> Observable<T>.bindSubscribe(
        onNext: (t: T) -> Unit,
        onComplete: () -> Unit = {},
        onError: (e: Throwable) -> Unit
    ) {
        subscribe(onNext, onError, onComplete).let { disposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        disposable = CompositeDisposable()
    }
}