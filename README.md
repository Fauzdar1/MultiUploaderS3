# MultiUploaderS3
This is to upload/download multiple files in/from Amazon S3 concurrently. 


### Initialize S3Client in [MultiUploaderS3](https://github.com/Fauzdar1/MultiUploaderS3/blob/32a8b5623ed1008776a568206d7eddb76a5b04d0/MultiUploderS3.kt#L20) or [MultiDownloaderS3](https://github.com/Fauzdar1/MultiUploaderS3/blob/96b5d73e18e36cb012ceb008cc9bada626dcdebb/MultiDownloaderS3.kt#L20) as 

    fun s3ClientInitialization(context: Context): AmazonS3 {
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

### Upload multiple files from your Activity as 

 

    val map: Map<File, String> = yourArrayList.map {it to Your_Key_For_Each_File}.toMap()
    MultiUploaderS3().uploadMultiple(map, this)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe {
            runOnUiThread {
                Toast(this@AddActivity, "Uploading completed", Toast.LENGTH_LONG).show()
            }
         }

### Download multiple files from your Activity as 




    val map: Map<File, String> = yourKeysArrayList.map {Your_FILE_For_Each_KEY to it}.toMap()
    MultiDownloaderS3().downloadMultiple(map, this)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .subscribe {
            runOnUiThread {
                Toast(this@AddActivity, "Downloading completed", Toast.LENGTH_LONG).show()
            }
         }

I've also provided this as an answer on StackOverflow - [Amazon s3 upload multiple files android](https://stackoverflow.com/a/62610546/8244632)

Here's the original answer provided by a member of AWS Amplify Organization from which I've implemented this - [Upload Multiple files using TransferUtility](https://github.com/aws-amplify/aws-sdk-android/issues/505#issuecomment-612187402)

