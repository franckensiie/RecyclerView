
import retrofit2.Response
import retrofit2.http.*

interface TasksWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<com.pkg.recyclerview.network.Task>>;

    @POST("tasks")
    suspend fun create(@Body task: com.pkg.recyclerview.network.Task): Response<com.pkg.recyclerview.network.Task>

    @PATCH("tasks/{id}")
    suspend fun update(@Body task: com.pkg.recyclerview.network.Task, @Path("id") id: String? = task.id): Response<com.pkg.recyclerview.network.Task>

    @DELETE("tasks/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>
}