package com.example.shimmerrecyclerview

class DI {
}

class TokenAuthenticator(
    private val tokenManager: TokenManager // A class that handles token storage and retrieval
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val newToken = tokenManager.refreshToken() // Implement this method to refresh the token

        return if (newToken != null) {
            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        } else {
            null // If the token cannot be refreshed, return null
        }
    }
}

class TokenInterceptor(
    private val tokenManager: TokenManager,
    private val context: Context // To access the NavController
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer ${tokenManager.getToken()}")
            .build()

        val response = chain.proceed(request)

        if (response.code == 401) { // Unauthorized
            // Navigate to login page
            val navController = (context as AppCompatActivity).findNavController(R.id.nav_host_fragment)
            navController.navigate(R.id.loginFragment) // Adjust this to your login fragment ID
        }

        return response
    }
}


@Singleton
@Provides
fun provideRetrofitBuilder(gsonBuilder: Gson, context: Context, tokenManager: TokenManager): Retrofit.Builder {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY // Set the desired log level

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(TokenInterceptor(tokenManager, context)) // Add the token interceptor
        .authenticator(TokenAuthenticator(tokenManager)) // Add the token authenticator
        .build()

    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
        .client(okHttpClient) // Set the OkHttpClient with the interceptor and authenticator
}


class TokenManager(private val context: Context) {

    fun getToken(): String? {
        // Retrieve the token from shared preferences or any other storage
        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    fun refreshToken(): String? {
        // Implement your token refresh logic here
        // For example, make a synchronous network call to refresh the token
        // Save the new token and return it
        val newToken = "newToken" // Replace this with actual refresh logic
        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", newToken).apply()
        return newToken
    }
}
