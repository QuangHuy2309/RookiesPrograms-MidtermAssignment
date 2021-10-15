export function convertBase64ToFile(image, filename) {
    if (image != null){
    // const byteString = atob(image.split(',')[1]);
    // const ab = new ArrayBuffer(byteString.length);
    // const ia = new Uint8Array(ab);
    // for (let i = 0; i < byteString.length; i += 1) {
    //   ia[i] = byteString.charCodeAt(i);
    // }
    var file = new File([image], filename, {type:'image/jpeg'});
    // const newBlob = new Blob([ab], {
    //   type: 'image/jpeg',
    // });
    return file;
    // return newBlob;
  }
  };