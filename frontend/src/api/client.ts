import axios from 'axios';
import type { DisasterEvent, UploadResult, Summary } from '../types/disaster';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

export const addEvent = (event: DisasterEvent) =>
  api.post<DisasterEvent>('/events', event).then(r => r.data);

export const uploadCsv = (file: File) => {
  const form = new FormData();
  form.append('file', file);
  return api.post<UploadResult>('/events/upload', form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  }).then(r => r.data);
};

export const downloadCsv = () =>
  api.get('/events', { responseType: 'blob' }).then(r => r.data as Blob);

export const getSummary = () =>
  api.get<Summary>('/events/summary').then(r => r.data);
