## QR Code Generator


- QR Code Generator is a Spring Boot backend application that allows users to generate QR Codes with different parameters.

## Features

- Generates QR Codes according to following parameters:

- Contents
- Size
- File type
- Error correction


## Usage

- query the api with the following endpoint:
- /api/qrcode?contents=**{content}**&size=**{size}**&type=**{type}**&correction=**{errorCorrection}**
- size, type and error correction are optional parameters, default values are set when parameters are absent.
