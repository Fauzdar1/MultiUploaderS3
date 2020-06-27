import android.content.Context
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
 import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
 import io.reactivex.Completable
 import io.reactivex.Observable
 import io.reactivex.Single
 import java.io.File

 class MultiUploaderS3 {

     private fun transferUtility(context: Context): Single<TransferUtility?>? {
         return Single.create { emitter ->
             emitter.onSuccess(
             TransferUtility(s3ClientInitialization(context), context)
             )
         }
     }
  
     private fun s3ClientInitialization(context: Context): AmazonS3 {
        val cognitoCachingCredentialsProvider = CognitoCachingCredentialsProvider(
            context,
            your key,
            region
        )
        return AmazonS3Client(
            cognitoCachingCredentialsProvider,
            Region.getRegion(Regions.YOUR_REGION)
        )
     }

     fun uploadMultiple(fileToKeyUploads: Map<File, String>, context: Context): Completable? {
         return transferUtility(context)!!
             .flatMapCompletable { transferUtility ->
                 Observable.fromIterable(fileToKeyUploads.entries)
                     .flatMapCompletable { entry ->
                         uploadSingle(
                             transferUtility,
                             entry.key,
                             entry.value
                         )
                     }
             }
     }

         private fun uploadSingle(
         transferUtility: TransferUtility,
         aLocalFile: File?,
         toRemoteKey: String?
     ): Completable? {
         return Completable.create { emitter ->
             transferUtility.upload(bucketName,toRemoteKey, aLocalFile)
                 .setTransferListener(object : TransferListener {
                     override fun onStateChanged(
                         id: Int,
                         state: TransferState
                     ) {
                         if (TransferState.FAILED == state) {
                             emitter.onError(Exception("Transfer state was FAILED."))
                         } else if (TransferState.COMPLETED == state) {
                             emitter.onComplete()
                         }
                     }

                     override fun onProgressChanged(
                         id: Int,
                         bytesCurrent: Long,
                         bytesTotal: Long
                     ) {
                     }

                     override fun onError(id: Int, exception: Exception) {
                         emitter.onError(exception)
                     }
                 })
         }
     }

 }
