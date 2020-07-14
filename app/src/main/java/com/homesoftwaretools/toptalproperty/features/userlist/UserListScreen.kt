package com.homesoftwaretools.toptalproperty.features.userlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.navigator.Navigator
import com.homesoftwaretools.toptalproperty.core.navigator.Routes
import com.homesoftwaretools.toptalproperty.core.ui.BaseFragment
import com.homesoftwaretools.toptalproperty.core.ui.BaseViewModel
import com.homesoftwaretools.toptalproperty.core.ui.onClick
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.core.utils.Toaster
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.features.userlist.adapter.UserListAdapter
import org.koin.android.ext.android.inject
import org.koin.core.inject

class UserListScreen : BaseFragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: UserListAdapter
    private lateinit var fab: FloatingActionButton

    override val layoutId = R.layout.user_list
    private val rp: ResourceProvider by inject()
    private val vm: UserListViewModel by scopedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        adapter = UserListAdapter(requireContext(), rp, onClick = vm::editUser)
        recycler.adapter = adapter
        vm.users.onChange(adapter::swapData)
        fab.onClick { vm.addUser() }
    }

    private fun initView(v: View) {
        recycler = v.findViewById(R.id.recycler)
        fab = v.findViewById(R.id.fab)
    }
}


class UserListViewModel(scopeId: String) : BaseViewModel(scopeId) {

    private val navigator: Navigator by scope.inject()
    private val toaster: Toaster by scope.inject()
    private val useCase: UserListUseCase by inject()

    val users: LiveData<List<User>> = MutableLiveData()

    init {
        useCase.getUsers()
            .bindSubscribe(
                onNext = (users as MutableLiveData)::postValue,
                onError = toaster::showError
            )
    }

    fun addUser() {
        navigator.push(Routes.USER_EDITOR)
    }

    fun editUser(authId: String) {
        navigator.push(Routes.USER_EDITOR, args = mapOf("authId" to authId))
    }

}