// 이미지 파일 체크 (onChange)
function imgCheck(img) {
  let host_img = document.getElementById('host_img')
  let host_img_small = document.getElementById('host_img_small')
  let fileInfo = img.files[0].name.split('\.')
  let fileExtension = fileInfo[fileInfo.length-1] // 파일 확장자

  if(fileExtension !== 'jpg' && fileExtension !== 'png') { // 확장자가 jpg 또는 png가 아닐경우
    document.getElementById('preview').setAttribute('src', 'img/host-default-img.webp')
    img.outerHTML = img.outerHTML; // 선택된 input:type=file 초기화
    host_img_small.removeAttribute('hidden')
    document.getElementById('host_img').focus()
    return
  } else {
    host_img_small.setAttribute('hidden', true)
  }
  
  let imgSize = img.files[0].size // 이미지 용량
  let maxSize = 2 * 1024 * 1024 // 2MB

  if(imgSize>maxSize) { // 이미지 용량이 2MB가 넘을 경우
    document.getElementById('preview').setAttribute('src', 'img/host-default-img.webp')
    img.outerHTML = img.outerHTML; // 선택된 input:type=file 초기화
    host_img_small.removeAttribute('hidden')
    host_img.focus()
    return
  } else { // 선택된 이미지 미리보기 실행
    host_img_small.setAttribute('hidden', true)
    if (img.files && img.files[0]) {
      const reader = new FileReader()
      reader.onload = function(e) {
        document.getElementById('preview').src = e.target.result
      }
      reader.readAsDataURL(img.files[0])
    } else {
      document.getElementById('preview').src = ""
    }
  }
}


const profileCheck = () => {
 location.href = "host-home.html"
}
