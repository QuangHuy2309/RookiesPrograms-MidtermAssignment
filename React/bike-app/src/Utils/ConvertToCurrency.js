export function numberFormat(value) {
    return new Intl.NumberFormat('en-EN', {
        style: 'currency',
        currency: 'USD'
      }).format(value);
}