package mobile.uz.brlock_mobile.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import mobile.uz.brlock_mobile.R
import mobile.uz.brlock_mobile.domain.edit.EditRequest
import mobile.uz.brlock_mobile.domain.login.Entity
import mobile.uz.brlock_mobile.domain.login.LoginRequest
import mobile.uz.brlock_mobile.domain.register.RegisterRequest
import mobile.uz.brlock_mobile.network.ApiInterface
import mobile.uz.brlock_mobile.network.ErrorResp
import mobile.uz.brlock_mobile.network.RetrofitClient
import mobile.uz.brlock_mobile.utils.Constants
import mobile.uz.brlock_mobile.utils.extentions.loge
import mobile.uz.brlock_mobile.utils.extentions.toast
import mobile.uz.brlock_mobile.utils.network.Errors
import mobile.uz.brlock_mobile.utils.preferences.SharedManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import retrofit2.HttpException

open class BaseViewModel(
    private val gson: Gson,
    private val context: Context,
    private val sharedManager: SharedManager
) : ViewModel(), KoinComponent {

    @LayoutRes
    var parentLayoutId: Int = 0

    @LayoutRes
    var navLayoutId: Int = 0

    val single = MutableLiveData<Any>()
    val error: MutableLiveData<ErrorResp> by inject(named("errorLive"))
    private val api = RetrofitClient
        .getRetrofit(Constants.BASE_URL, getToken(), context, gson)
        .create(ApiInterface::class.java)

    private val compositeDisposable = CompositeDisposable()

    private fun parseError(e: Throwable?) {
        var message = context.resources.getString(R.string.smth_wrong)
        if (e != null && e.localizedMessage != null) {
            loge(e.localizedMessage)
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                errorBody?.let {
                    try {
                        loge(it)
                        val errors = it.split(":")
                            .filter { it.contains("[") }
                        val errorsString = if (errors.isNotEmpty()) {
                            errors.toString()
                                .replace("[", "")
                                .replace(",", "\n")
                                .replace("]", "")
                                .replace("{", "")
                                .replace("}", "")
                                .replace("\"", "")
                        } else {
                            val resp = it.split(":")
                            if (resp.size >= 2) resp[1].replace("{", "")
                                .replace("}", "")
                                .replace("\"", "")
                            else it
                        }

                        message = if (errorsString.isEmpty())
                            context.resources.getString(R.string.smth_wrong)
                        else errorsString

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else message = Errors.traceErrors(e, context)
        }
        toast(context, message)
        error.value = ErrorResp(message)
    }

    @SuppressLint("CheckResult")
    fun newThread(action: () -> Unit) {
        Observable.fromCallable { action() }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, { e ->
                e.printStackTrace()
                parseError(e)
            })
    }


    /*  fun fetchData(): MutableLiveData<Boolean> {
          if (sharedManager.token.isNotEmpty()) {
              val gson = Gson()
              val type = object : TypeToken<LoginResponse>() {}.type
              val fromJson = gson.fromJson<LoginResponse>(sharedManager.token, type)
              shared.value = fromJson.result
              logi("Current token : " + fromJson.data!!.authToken)
          } else shared.value = false
          return shared
      }*/

    fun fetchUser(): Entity {
        if (sharedManager.token.isNotEmpty()) {
            val type = object : TypeToken<Entity>() {}.type
            return gson.fromJson(sharedManager.token, type)
        }
        return Entity("", null)
    }

    private fun getToken(): String {
        if (sharedManager.token.isNotEmpty()) {
            val type = object : TypeToken<Entity>() {}.type
            val fromJson = gson.fromJson<Entity>(sharedManager.token, type)
            return  "Bearer ${fromJson.authToken}"
        }
        return "Bearer "
    }

    fun register(
        name: String,
        surname: String,
        phoneNumber: String,
        carNumber: String,
        password: String,
        roles: List<String>
    ) = compositeDisposable.add(
        api
            .register(RegisterRequest(name, surname, phoneNumber, carNumber, password, roles))
            .observeAndSubscribe()
            .subscribe(
                {
                    single.value = it
                },
                {
                    parseError(it)
                }
            )
    )

    fun login(phone: String, password: String) =
        compositeDisposable.add(
            api.login(LoginRequest(phone, password)).observeAndSubscribe()
                .subscribe(
                    {
                        val loginResponse = it.entities[0]
                        val toJson = gson.toJson(loginResponse)
                        sharedManager.token = toJson.toString()
                        single.value = it
                    },
                    {
                        parseError(it)
                    }
                )
        )

    fun editUser(
        id: Int,
        name: String,
        surname: String,
        phoneNumber: String,
        carNumber: String
    ) = compositeDisposable.add(
        api.editUser(EditRequest(id, name, surname, phoneNumber, carNumber))
            .observeAndSubscribe()
            .subscribe(
                {
                    val editUserResponse = it.entities[0]
                    val type = object : TypeToken<Entity>() {}.type
                    val fromJson = gson.fromJson<Entity>(sharedManager.token, type)
                    fromJson.userJpo?.name = editUserResponse.name
                    fromJson.userJpo?.surname = editUserResponse.surname
                    fromJson.userJpo?.phoneNumber = editUserResponse.phoneNumber
                    fromJson.userJpo?.carNumber = editUserResponse.carNumber
                    val toJson = gson.toJson(fromJson)
                    sharedManager.token = toJson.toString()
                    single.value = it
                    fetchUser()
                },
                {
                    parseError(it)
                }
            )
    )

    fun getByPhoneNumber(phoneNumber: String) =
        compositeDisposable.add(
            api.searchByPhoneNumber(phoneNumber).observeAndSubscribe()
                .subscribe(
                    {
                        single.value = it
                    },
                    {
                        parseError(it)
                    }
                )
        )

    fun getAll() = compositeDisposable.add(
        api.getAll().observeAndSubscribe()
            .subscribe(
                {
                    val toJson = gson.toJson(it)
                    single.value = toJson
                    loge(it, "users")
                },
                {
                    parseError(it)
                })
    )

    fun logOut() = compositeDisposable.add(
        api.logout().observeAndSubscribe()
            .subscribe(
                {
                    sharedManager.deleteAll()

                },
                {
                    parseError(it)
                }
            )
    )


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

fun <T> Single<T>.observeAndSubscribe() =
    subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
