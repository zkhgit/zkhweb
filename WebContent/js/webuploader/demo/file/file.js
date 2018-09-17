(function( $ ){
	// 当domReady的时候开始初始化
    $(function() {
		var $list = $('#thelist'),
			$btn = $('#ctlBtn'),
			state = 'pending',
			uploader;
		
		uploader = WebUploader.create({
			// 选完文件后，是否自动上传。
			// auto:true,
	
		    // 文件接收服务端。
		    server: 'http://localhost:8080/zkhweb/upload',
		    
		    // 传参
		    formData: {
		       filePath: 'G:\\tempFilePath22'
		    },
	
		    pick: {
		    	id: '#picker',      // 选择文件的按钮，内部根据当前运行是创建，可能是input元素，也可能是flash.
		    	name:"fileName",    //这个地方 name 没什么用
		        label: '选文件按钮文字',
		        multiple:true       //默认为true，就是可以多选
		    },
		    
		    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		    resize: false,
		    
		    // swf文件路径
		    swf: '../../Uploader.swf',
		    
		    // 限制只能上传一个文件
		    // fileNumLimit: 1,
	
		    // 限制大小10M，单文件
		    fileSingleSizeLimit: 10*1024*1024,
		    
		    // 限制大小10M，所有被选文件，超出选择不上
		    fileSizeLimit: 10*1024*1024,
	
		    // 只允许选择图片
		    /**
		    accept: {
			    title: '图片',
			    extensions: 'gif,jpg,jpeg,bmp,png',
			    mimeTypes: 'image/*'
		    },
			*/
		    
		    // 只允许选择excel表格文件。
		    /**
		    accept : {
		        title : 'Applications',
		        extensions : 'xls,xlsx',
		        mimeTypes : 'application/xls,application/xlsx'
		    }
		    */
		    
		    // 只允许选择pdf、word
		    /**
		    accept: {  
		        title: 'Files',  
		        extensions: 'pdf,doc,docx',
		        mimeTypes: 'application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/pdf'  
		    }
		    */
		});
		// 初始化以后添加参数
		//uploader.options.formData.uid = 123;
	
		// 上传前的判断处理
		uploader.on('error', function( type ){
		    if ( type === 'Q_EXCEED_NUM_LIMIT' ) {
		        alert('文件超过最大允许上传的数量');
		    } 
		    if ( type === 'F_DUPLICATE' ) {
		        alert('文件重复，不能上传！');
		    }
		    if (type == 'Q_TYPE_DENIED'){
		        alert("请上传指定格式文件");
		    }else if(type == 'F_EXCEED_SIZE'){
		        alert('文件大小超出限制');
		    }
		});
	
		// 当有文件被添加进队列的时候
		uploader.on( 'fileQueued', function( file ) {
		    $list.append( 
			    '<div id="' + file.id + '" class="item">' +
			        '<h4 class="info webuploadDelbtn">' + file.name + '</h4>' +
			        '<span class="state">等待上传...</span>' +
			    '</div>' 
		    );
		});
	
		//上传按钮控制
		$btn.on( 'click', function() {
		    if ( state === 'uploading' ) {
		        uploader.stop();
		    } else {
		        uploader.upload();
		    }
		});
	
		// 上传前
		uploader.on( 'uploadBeforeSend', function( block, data ) {
			// 传递参数
			data.name = '参数';
		});
	
		// 文件上传过程中创建进度条实时显示。
		uploader.on( 'uploadProgress', function( file, percentage ) {
		    var $li = $( '#'+file.id ), $percent = $li.find('.progress .progress-bar');
	
		    // 避免重复创建
		    if ( !$percent.length ) {
		        $percent = $(
			        '<div class="progress progress-striped active">' +
			          '<div class="progress-bar" role="progressbar" style="width: 0%">' +
			          '</div>' +
			        '</div>'
		        ).appendTo( $li ).find('.progress-bar');
		    }
	
		    $li.find('p.state').text('上传中');
	
		    $percent.css( 'width', percentage * 100 + '%' );
		});
	
		// 上传成功、失败处理
		uploader.on( 'uploadSuccess', function( file , response ) {
			console.log(response);
		    $( '#'+file.id ).find('p.state').text('已上传');
		});
	
		// 上传错误
		uploader.on( 'uploadError', function( file ) {
		    $( '#'+file.id ).find('p.state').text('上传出错');
		});
	
		// 上传完成
		uploader.on( 'uploadComplete', function( file ) {
		    $( '#'+file.id ).find('.progress').fadeOut();
		    $('.webuploadDelbtn').css('display', 'none');
		});
	
		// 全部上传完成
		uploader.on( 'all', function( type ) {
		    if ( type === 'startUpload' ) {
		        state = 'uploading';
		    } else if ( type === 'stopUpload' ) {
		        state = 'paused';
		    } else if ( type === 'uploadFinished' ) {
		        state = 'done';
		    }
	
		    if ( state === 'uploading' ) {
		        $btn.text('上传中');
		    } else {
		        $btn.text('开始上传');
		    }
		});
	
		
		//删除
		$list.on("click", ".webuploadDelbtn", function () {
		  var $ele = $(this);
		  var id = $ele.parent().attr("id");
		  var file = uploader.getFile(id);
		  uploader.removeFile(file,true);  
		}); 
	
		//删除时执行的方法
		uploader.on('fileDequeued', function (file) {
		  $(file.id).remove();
		  $('#'+file.id ).find('span.state').text('已经取消');
		  $('#'+file.id).hide();   
		  console.log("remove");     
		});
		
    });
})( jQuery );