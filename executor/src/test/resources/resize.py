import os
from PIL import Image

def main():
  file = 'big_image.png'
  file_stats = os.stat(file)
  print('Initial image size and dimension\n')
  printImage(file_stats)
  with Image.open(file) as im:
    print(im.format, im.size, im.mode)
    resized = im.resize((400,400))
    resized.save('resized_'+file)
  newImage = os.stat('resized_'+file)
  print('Final image size')
  printImage(newImage)  

def printImage(image):
  print(f'File Size in Bytes is {image.st_size}')
  print(f'File Size in Kbytes is {image.st_size / 1024 : .2f}')
  print(f'File Size in MegaBytes is {image.st_size / (1024 * 1024) : .2f}')


if __name__ == "__main__":
    main()
