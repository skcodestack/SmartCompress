# SmartCompress -- 灵巧的图片压缩工具

## 1.描述

### SmartCompress一个小巧的图片压缩框架，现在的手机拍照小则1,2M，大则4M以上，对于这种情况，移动设备的多图片上次就变得吃力了，不仅上传耗时长，还浪费流量。
	

### 优点：
	
- 使用简单
- 压缩参数可配置
- 链式调用
- 提供常用的回调接口
- 不依赖第三方库

## 2.导入
    
    compile 'com.skcodestack:smartcompress:1.0'

## 3.方法介绍

方法 | 描述
---- | ----
create | 创建SmartCompress
load	| 添加要压缩的图片
quality | 设置压缩图片质量
mode | 设置结果回调的模式
timeout | 设置压缩图片的超时时间 
dir | 缓存压缩图片路径
width/height | 进行降采样率压缩的图片宽高
setCompressListener | 压缩回调接口【主线程】
setProgressListener | 压缩开始结束回调监听【主线程】
cancle | 取消当前压缩任务
shutdown | 关闭所有压缩的任务
execute | 开始压缩



### QualityLevel  压缩质量

参数 | 描述
---- | ----
LOW	| 低
NORMAL| 中
HIGHT| 高


### ResultMode    结果回调模式

参数 | 描述
---- | ----
STRICT	| 严格模式 只有全部成功才成功
NORMAL|正常模式 一个成功就成功，不成功的返回原图片地址



### OnProgressListener    压缩开始结束回调监听【主线程】

方法 | 描述
---- | ----
onStart	| 压缩开始前回调
onEnd	| 压缩结束回调，包括取消任务

### setCompressListener    压缩回调接口【主线程】

方法 | 描述
---- | ----
onSuccess	| 压缩成功，返回压缩后图片路径
onError	| 压缩失败，返回原因

1.返回压缩后的列表
	public void onSuccess(List<String> paths)； 
2.返回压缩后其中一张（适合只压缩一张图片的情况）
 	public void onSuccess(String path)
3.可以获取出错的原因
	public void onError(ErrorCode code){
		String errorMsg = code.getMessage();
	}

### ErrorCode    错误码

参数 | 描述
---- | ----
COMPRESS_FAILED	| 图片压缩失败
NON_WRITE_STORAGE_PERMISSION|没有读写存储设备权限
COMPRESS_TIME_OUT	| 压缩超时
INTERRUPTED	| 压缩图片线程被中断

1.获取错误信息：
	ErrorCode.getMessage()



## 4.使用

### 4.1 压缩图片

	 SmartCompress.create(this)
				.smartCompress.dir(dir)
                .width(960)
                .load(imageList)
                .mode(ResultMode.NORMAL)
                .timeout(2000)
                .quality(QualityLevel.LOW)
                .setProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                    }

                    @Override
                    public void onEnd() {
                        progressDialog.dismiss();
                    }
                })
                .setCompressListener(new OnDefaultCompressListener() {
                    @Override
                    public void onSuccess(List<String> paths) {
                        super.onSuccess(paths);
                        Log.e("ttt", "compress image file success :" + paths);
                        Toast.makeText(MainActivity.this,"压缩成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ErrorCode code) {
                        super.onError(code);
                        Log.e("ttt", "compress image file  faile:" + code);
                        Toast.makeText(MainActivity.this,"压缩失败，失败原因："+ code.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
                .execute();


### 4.2 取消当前压缩任务
	
		smartCompress.cancle();

### 4.3 取消所有压缩认为

		SmartCompress.shutdown();

## link

[SmartCompress](https://github.com/skcodestack/SmartCompress)


## License

    Copyright 2018 skcodestack
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.