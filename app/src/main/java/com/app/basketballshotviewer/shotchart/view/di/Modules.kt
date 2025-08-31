import com.app.basketballshotviewer.shotchart.data.datasource.ShotDataSource
import com.app.basketballshotviewer.shotchart.data.repo.ShotRepository
import com.app.basketballshotviewer.shotchart.data.repo.ShotRepositoryImpl
import com.app.basketballshotviewer.shotchart.domain.usecase.LiveShotsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/* Template file for dependency injections */

@Module
@InstallIn(SingletonComponent::class)
object Modules {
    @Provides @Singleton
    fun provideShotDataSource(): ShotDataSource = ShotDataSource()

    @Provides @Singleton
    fun provideShotsRepository(
        sdk: ShotDataSource
    ): ShotRepository = ShotRepositoryImpl(sdk)

    @Provides @Singleton
    fun provideLiveShotsUseCase(
        repo: ShotRepository
    ) = LiveShotsUseCase(repo)
}
